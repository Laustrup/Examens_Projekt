package patrick_laust_ayo.examproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import patrick_laust_ayo.examproject.models.*;
import patrick_laust_ayo.examproject.services.*;
import patrick_laust_ayo.examproject.services.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ProjectController {

    private ProjectCreator projectCreator = new ProjectCreator();
    private ProjectEditor projectEditor = new ProjectEditor();
    private ExceptionHandler handler = new ExceptionHandler();


    @GetMapping("/create_project/{projectmanager_username}")
    public String renderCreateProject(@PathVariable(name = "projectmanager_username") String username,
                                      HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        session.setAttribute("current_project","start");
        model.addAttribute("current_project", session.getAttribute("current_project"));
        return "create_project";
    }


    @PostMapping("/create-project")
    public String createProject(@RequestParam(name = "title") String title,
                                HttpServletRequest request,Model model) {

        HttpSession session = request.getSession();
        String username = ((ProjectManager) session.getAttribute("projectManager")).getUsername();

        String inputException = handler.isLengthAllowedInDatabase(title,"title");

        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "create_project/" + username;
        }
        if (handler.doesProjectExist(title)) {
            model.addAttribute("Exception","Project already exists...");
            return "create_project/" + username;
        }

        session.setAttribute("project", projectCreator.createProject(title, ((ProjectManager)session.getAttribute("projectManager")).getUsername()));
        projectCreator.currentPhaseNo = 0;

        return "redirect:/add_participant/projectmanager/" + title;
    }

    @PostMapping("/update_project")
    public String updateProject(@RequestParam(name="new_project_title") String newTitle, HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();
        String projectTitle = ((Project)session.getAttribute("project")).getTitle();
        String participantUserId = ((Participant) session.getAttribute("participant")).getId();


        if (handler.isParticipantProjectManager(participantUserId)) {
            String exception = handler.isLengthAllowedInDatabase(newTitle, "title");
            if (!exception.equals("Input is allowed")) {
                if (handler.doesProjectExist(newTitle)) {
                    session.setAttribute("Exception","Project already exists...");
                    return "redirect:/project_page-" + projectTitle + "/" + participantUserId;
                }
                session.setAttribute("Exception", exception);
                return "redirect:/project_page-" + projectTitle + "/" + participantUserId;
            }
            session.setAttribute("project", projectEditor.updateProject(newTitle, projectTitle));
            return "redirect:/project_page-" + newTitle + "/" + participantUserId;
        }
        session.setAttribute("Exception", "You are not project manager...");
        return "redirect:/project_page-" + projectTitle + "/" + participantUserId;

    }

    @PostMapping("/direct_project_page")
    public String goToChoosenProjectPage(@RequestParam(name = "project_title") String projectTitle, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        String participantId = ((Participant) session.getAttribute("participant")).getId();

        session.setAttribute("current_project","start");
        model.addAttribute("current_project", session.getAttribute("current_project"));

        return "redirect:/project_page-" + projectTitle + "/" + participantId;
    }

    // TODO Does all three cases work with this endpoint? Redirect needs html
    // Have changed project_page/ to project_page-
    @GetMapping("/project_page-{project_title}/{user_id}")
    public String renderProjectpage(@PathVariable(name = "project_title") String projectTitle,
                                    @PathVariable(name = "user_id") String userId,
                                    HttpServletRequest request,Model model) {

        HttpSession session = request.getSession();

        if (session.getAttribute("participant")==null && handler.doesProjectExist(projectTitle)) {
            session.setAttribute("project",projectCreator.getProject(projectTitle));
            session.setAttribute("current_login","with_invite");
            return "redirect:/participant_login_page";
        }
        else if (handler.isParticipantPartOfProject(userId, projectTitle)) {

            Project project = projectCreator.getProject(projectTitle);
            session.setAttribute("project",project);

            model.addAttribute("project",project);
            model.addAttribute("work_hours",project.getTotalWorkhours());
            model.addAttribute("total_cost",project.getTotalCost());
            model.addAttribute("phase", session.getAttribute("phase"));

            model.addAttribute("participant",new UserCreator().getParticipant(userId));
            session.setAttribute("participant", model.getAttribute("participant"));

            model.addAttribute("current",session.getAttribute("current"));
            model.addAttribute("current_project",session.getAttribute("current_project"));
            model.addAttribute("Exception", session.getAttribute("Exception"));


            return "project_page";
        }

        session.setAttribute("Exception","Project doesn't exist...");
        return "redirect:/";
    }

    @PostMapping("/project_page_update-participant_pressed")
    public String participantUpdatedInProject(@RequestParam (name = "participant_ID") String userId,
                                                    @RequestParam (name = "participant_password") String password,
                                                    @RequestParam (name = "participant_name") String name,
                                                    @RequestParam (name = "position") String position,
                                                    @RequestParam (name = "department") String depName,
                                                    HttpServletRequest request, Model model){

        HttpSession session = request.getSession();
        UserEditor userEditor = new UserEditor();
        String formerUserId = ((Participant) session.getAttribute("participant")).getId();
        String projectTitle = ((Project) session.getAttribute("project")).getTitle();

        String exception = handler.isLengthAllowedInDatabase(userId,"user_id");
        if (!exception.equals("Input is allowed")) {
            session.setAttribute("Exception",exception);
            return "redirect:/project_page-" + projectTitle + "/" + formerUserId;
        }

        if (handler.doesUserIdExist(userId) && !userId.equals("")) {
            session.setAttribute("Exception","User-id already in use...");
            return "redirect:/project_page-" + projectTitle + "/" + formerUserId;
        }

        exception = handler.isLengthAllowedInDatabase(password,"participant_password");
        if (!exception.equals("Input is allowed")) {
            session.setAttribute("Exception",exception);
            return "redirect:/project_page-" + projectTitle + "/" + formerUserId;
        }

        exception = handler.isLengthAllowedInDatabase(name,"participant_name");
        if (!exception.equals("Input is allowed")) {
            session.setAttribute("Exception",exception);
            return "redirect:/project_page-" + projectTitle + "/" + formerUserId;
        }

        exception = handler.isLengthAllowedInDatabase(position,"position");
        if (!exception.equals("Input is allowed")) {
            session.setAttribute("Exception",exception);
            return "redirect:/project_page-" + projectTitle + "/" + formerUserId;
        }

        if (userId.equals("")){
            session.setAttribute("participant" ,userEditor.updateParticipant(formerUserId, password, name,
                    position, depName,formerUserId, handler.isParticipantProjectManager(formerUserId)));
            userId = formerUserId;
        }
        else {
            session.setAttribute("participant", userEditor.updateParticipant(userId, password, name,
                    position, depName, formerUserId, handler.isParticipantProjectManager(formerUserId)));
        }

        Participant participant = (Participant) session.getAttribute("participant");

        model.addAttribute("participant", participant);

        return "redirect:/project_page-" + projectTitle + "/" + userId;
    }


    @PostMapping("/direct_to_add_participants")
    public String directToAddParticipants(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String projectTitle = ((Project)session.getAttribute("project")).getTitle();
        String participantId = ((Participant)session.getAttribute("participant")).getId();
        session.setAttribute("Exception","");
        session.setAttribute("Message","");
        session.setAttribute("current_project","add_participant");
        model.addAttribute("current_project", session.getAttribute("current_project"));
        System.out.println("DirectToAddParticipants " + session.getAttribute("current_project"));

        return "redirect:/project_page-" + projectTitle + "/" + participantId;
    }

    /*
    @GetMapping("/project_page-{project_title}/{user_id}/add_participants")
    public String renderAddParticipants(Model model,HttpServletRequest request) {
        HttpSession session = request.getSession();
        model.addAttribute("project",session.getAttribute("project"));
        model.addAttribute("phase",session.getAttribute("phase"));
        model.addAttribute("assignment",session.getAttribute("assignment"));
        model.addAttribute("task",session.getAttribute("task"));
        model.addAttribute("current_project","add_participants");

        return "project_page";
    }

     */


    @GetMapping("/accept_delete_of_project")
    public String renderDeleteProject(Model model,HttpServletRequest request) {
        HttpSession session = request.getSession();
        model.addAttribute("Object_to_delete",session.getAttribute("project"));
        model.addAttribute("current_delete", "project");
        model.addAttribute("Exception","");
        model.addAttribute("Message","");
        return "accept_delete";
    }

    @PostMapping("/delete_project")
    public String deleteProject(@RequestParam(name = "password_delete_project") String password,
                                @RequestParam(name = "user_id") String userId, HttpServletRequest request) {

        HttpSession session = request.getSession();
        String projectTitle = ((Project)session.getAttribute("project")).getTitle();

        if (handler.isParticipantPartOfProject(userId,projectTitle)) {
            if (handler.allowLogin(userId, password)) {
                projectEditor.deleteProject(projectTitle);
                session.setAttribute("Message","Project is now deleted");
                session.setAttribute("Exception","");
                return "redirect:/";
            }
            session.setAttribute("Exception","Wrong user-id or password...");
            return "redirect:/accept_delete_of_" + projectTitle;
        }

        session.setAttribute("Exception","You are not part of project...");
        return "redirect:/accept_delete_of_" + projectTitle;
    }

    @PostMapping("/add_phase")
    public String addPhase(HttpServletRequest request) {

        HttpSession session = request.getSession();

        String projectTitle = ((Project)session.getAttribute("project")).getTitle();
        String userId = ((Participant)session.getAttribute("participant")).getId();

        session.setAttribute("phase", projectCreator.createPhase(projectTitle));
        System.err.println(((Phase) session.getAttribute("phase")).getTitle());
        return "redirect:/project_page-" + projectTitle + "/" + userId;
    }

    @PostMapping("/update_phase")
    public String updatePhase(@RequestParam(name="new_title") String newTitle, HttpServletRequest request) {

        HttpSession session = request.getSession();
        String projectTitle = ((Project)session.getAttribute("project")).getTitle();
        String phaseTitle = ((Phase)session.getAttribute("phase")).getTitle();

        String exception = handler.isLengthAllowedInDatabase(newTitle, "phase_title");
        if (exception.equals("Input is allowed")) {

            if (handler.doesPhaseExist(newTitle,phaseTitle)) {
                session.setAttribute("Exception","Phase already exists...");
                return "redirect:/project_page-" + projectTitle + "/" + phaseTitle;
            }
            Phase phase = projectEditor.updatePhase(newTitle, phaseTitle, projectTitle);
            session.setAttribute("phase",phase);
            session.setAttribute("Exception","");
            return "redirect:/project_page-" + projectTitle + "/" + phase.getTitle();
        }

        session.setAttribute("Exception", exception);
        session.setAttribute("Message", "Title of phase has changed!");
        return "redirect:/project_page-" + projectTitle + "/" + phaseTitle;

    }

    @PostMapping("/direct_to_phases")
    public String directToPhasesOfProject(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String projectTitle = ((Project)session.getAttribute("project")).getTitle();
        session.setAttribute("current","start");
        model.addAttribute("current", session.getAttribute("current"));
        session.setAttribute("Exception","");
        session.setAttribute("Message","");

        return "redirect:/project_page-" + projectTitle;
    }

    // TODO Perhaps make submitvalue with both title and split in method?
    @PostMapping("/direct_to_phase")
    public String directToPhase(@RequestParam(name="phase_title") String phaseTitle, HttpServletRequest request) {

        HttpSession session = request.getSession();
        String projectTitle = ((Project)session.getAttribute("project")).getTitle();
        String userId = ((Participant)session.getAttribute("participant")).getId();

        session.setAttribute("current","phase");
        session.setAttribute("phase",projectCreator.getPhase(phaseTitle,projectTitle));
        session.setAttribute("Exception","");
        session.setAttribute("Message","");

        return "redirect:/project_page-" + projectTitle + "/" + userId + "/" + phaseTitle;
    }

    // TODO Create phase html
    @GetMapping("/project_page-{project_title}/{user_id}/{phase_title}")
    public String renderPhase(@PathVariable(name = "project_title") String projectTitle,
                              @PathVariable(name = "user_id") String user_Id,
                              @PathVariable(name = "phase_title") String phaseTitle,
                              HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();
        //session.setAttribute("phase",projectCreator.getPhase(phaseTitle,projectTitle));
        System.out.println("GetPhaseTitle is " + projectCreator.getPhase(phaseTitle,projectTitle).getTitle());
        System.err.println(phaseTitle);

        model.addAttribute("project",projectCreator.getProject(projectTitle));

        model.addAttribute("phase",projectCreator.getPhase(phaseTitle,projectTitle));
        model.addAttribute("participant",session.getAttribute("participant"));
        model.addAttribute("Exception",session.getAttribute("Exception"));
        model.addAttribute("Message",session.getAttribute("Message"));
        model.addAttribute("current","phase");
        model.addAttribute("current_project","start");

        return "project_page";
    }

    @GetMapping("/accept_delete_of_{phase.getTitle()}")
    public String renderDeletePhase(Model model,HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("Exception","");
        session.setAttribute("Message","");
        model.addAttribute("Object_to_delete",((Phase)session.getAttribute("Phase")));
        return "accept_delete";
    }

    // TODO Does deletePhase also delete assignments and tasks of phase?
    @PostMapping("/delete_phase")
    public String deletePhase(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String phaseTitle = ((Phase)session.getAttribute("Phase")).getTitle();
        String projectTitle = ((Project)session.getAttribute("project")).getTitle();

        session.setAttribute("Exception","");
        session.setAttribute("Message",phaseTitle + " is now deleted!");
        projectEditor.deletePhase(phaseTitle,projectTitle);
        return "redirect:/project_page-"+projectTitle+"/" + ((Participant)session.getAttribute("participant")).getId();
    }

    @PostMapping("/add_assignment")
    public String addAssignment(@RequestParam(name="title") String title,
                                @RequestParam(name="start") String start,
                                @RequestParam(name="end") String end,
                                HttpServletRequest request) {

        HttpSession session = request.getSession();

        String projectTitle = ((Project)session.getAttribute("project")).getTitle();
        String phaseTitle = ((Phase)session.getAttribute("phase")).getTitle();

        String exception = handler.isLengthAllowedInDatabase(title,"assignment_title");
        if (!exception.equals("Input is allowed")) {
            session.setAttribute("Exception",exception);
            return "redirect:/project_page-" + projectTitle  + "/" + phaseTitle;
        }

        if (handler.isDateTimeCorrectFormat(start)) {
            session.setAttribute("Exception","Start should have the format yyyy-mm-dd hh-mm-dd");
            return "redirect:/project_page-" + projectTitle  + "/" + phaseTitle;
        }

        if (handler.isDateTimeCorrectFormat(end)) {
            session.setAttribute("Exception","End should have the format yyyy-mm-dd hh-mm-dd");
            return "redirect:/project_page-" + projectTitle  + "/" + phaseTitle;
        }

        Assignment assignment = projectCreator.createAssignment(phaseTitle,title,start,end);
        session.setAttribute("Exception","");
        session.setAttribute("Message","");

        return "redirect:/project_page-" + projectTitle + "/" + phaseTitle + "/" + assignment.getTitle();
    }

    @PostMapping("/update_assignment")
    public String updateAssignment(@RequestParam(name="new_title") String newTitle,
                                   @RequestParam(name="start") String start,
                                   @RequestParam(name="end") String end,
                                   HttpServletRequest request) {

        HttpSession session = request.getSession();
        String projectTitle = ((Project) session.getAttribute("project")).getTitle();
        String phaseTitle = ((Phase) session.getAttribute("phase")).getTitle();
        String assignmentTitle = ((Assignment) session.getAttribute("assignment")).getTitle();

            String exception = handler.isLengthAllowedInDatabase(newTitle, "assignment_title");
            if (!exception.equals("Input is allowed")) {
                session.setAttribute("Exception", exception);
                return "redirect:/project_page-" + projectTitle + "/" + phaseTitle + "/" + assignmentTitle;
            }

            if (handler.isDateTimeCorrectFormat(start)) {
                session.setAttribute("Exception", "Start should have the format yyyy-mm-dd hh-mm-dd");
                return "redirect:/project_page-" + projectTitle + "/" + phaseTitle + "/" + assignmentTitle;
            }

            if (handler.isDateTimeCorrectFormat(end)) {
                session.setAttribute("Exception", "End should have the format yyyy-mm-dd hh-mm-dd");
                return "redirect:/project_page-" + projectTitle + "/" + phaseTitle + "/" + assignmentTitle;
            }

            Assignment assignment = projectEditor.updateAssignment(newTitle, start, end, assignmentTitle, phaseTitle);

            if (handler.doesAssignmentExist(assignment.getTitle(),phaseTitle)) {
                session.setAttribute("Exception","Assignment already exists");
                return "redirect:/project_page-" + projectTitle + "/" + phaseTitle + "/" + assignmentTitle;
            }
            session.setAttribute("assignment", assignment);
            session.setAttribute("Exception","");
            session.setAttribute("Message","Assignment is now updated!");

            return "redirect:/project_page-" + projectTitle + "/" + phaseTitle + "/" + assignment.getTitle();

    }

    @PostMapping("/change_assignment_is_completed_status")
    public String changeAssignmentIscompletedStatus(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String phaseTitle = ((Phase)session.getAttribute("phase")).getTitle();
        Assignment assignment = projectCreator.getAssignment(((Assignment)session.getAttribute("assignment")).getTitle(),
                                                            ((Phase)session.getAttribute("phase")).getTitle());
        if (assignment.isCompleted()) {
            projectEditor.changeIsCompletedAssignment(false,assignment.getTitle(),((Phase)session.getAttribute("phase")).getTitle());
        }
        else {
            projectEditor.changeIsCompletedAssignment(true,assignment.getTitle(),((Phase)session.getAttribute("phase")).getTitle());
            for (int i = 0; i < assignment.getTasks().size(); i++) {
                Task task = assignment.getTasks().get(i);
                if (!task.isCompleted()) {
                    projectEditor.changeIsCompletedTask(true,task.getTitle(),task.getStart(),task.getEnd(),phaseTitle);
                }
            }
        }

        return "redirect:/project_page-" + ((Project)session.getAttribute("project")).getTitle() + "/" +
                ((Phase)session.getAttribute("phase")).getTitle() + "/" + assignment.getTitle();
    }

    @PostMapping("/direct_to_assignment")
    public String directToAssignment(@RequestParam(name="assignment_title_search") String assignmentTitle,
                                     HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("assignment",projectCreator.getAssignment(assignmentTitle,((Phase)session.getAttribute("phase")).getTitle()));
        session.setAttribute("Exception","");
        session.setAttribute("Message","");
        return "redirect:/project_page-" + ((Project)session.getAttribute("project")).getTitle() + "/" +
                ((Phase)session.getAttribute("phase")).getTitle() + "/" +  assignmentTitle;
    }


    @GetMapping("/projectpage-{project_title}/{phase_title}/{assignment_title}")
    public String renderAssignment(@PathVariable(name = "project_title") String projectTitle,
                                   @PathVariable(name = "phase_title") String phaseTitle,
                                   @PathVariable(name = "assignment_title") String assignmentTitle,
                                   HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();
        //session.setAttribute("assignment",projectCreator.getAssignment(assignmentTitle,phaseTitle));

        System.err.println("render assignment getproject title er " + projectCreator.getProject(projectTitle).getTitle());
        System.err.println("project page med assignment, titlen er " + assignmentTitle);

        model.addAttribute("project",projectCreator.getProject(projectTitle));
        System.err.println("project page med phase og assignment, projectTitle " + projectCreator.getProject(projectTitle).getTitle());

        model.addAttribute("phase",projectCreator.getPhase(phaseTitle,projectTitle));
        System.err.println("project page med phase og assignment, phaseTitle " + projectCreator.getPhase(phaseTitle, projectTitle).getTitle());

        model.addAttribute("assignment",projectCreator.getAssignment(assignmentTitle, projectTitle));
        System.err.println("project page med phase og assignment, assignmentTitle " + projectCreator.getAssignment(assignmentTitle, projectTitle).getTitle());

        model.addAttribute("Exception",session.getAttribute("Exception"));
        model.addAttribute("Message",session.getAttribute("Message"));
        model.addAttribute("current","assignment");
        model.addAttribute("current_project","start");

        return "project_page";
    }

    @GetMapping("/accept_delete_of_{assignment.getTitle()}")
    public String renderDeleteAssignment(Model model,HttpServletRequest request) {
        HttpSession session = request.getSession();
        model.addAttribute("Object_to_delete",session.getAttribute("Assignment"));
        model.addAttribute("Exception",session.getAttribute("Exception"));
        model.addAttribute("Message",session.getAttribute("Message"));
        return "accept_delete";
    }

    @PostMapping("/delete_assignment")
    public String deleteAssignment(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String projectTitle = ((Project)session.getAttribute("project")).getTitle();
        String phaseTitle = ((Phase)session.getAttribute("phase")).getTitle();
        projectEditor.deleteAssignment(((Assignment)session.getAttribute("assignment")).getTitle(),phaseTitle);
        return "redirect:/project_page-" + projectTitle + "/" + phaseTitle;
    }

    @PostMapping("/add_task")
    public String addTask(HttpServletRequest request) {

        HttpSession session = request.getSession();

        String projectTitle = ((Project)session.getAttribute("project")).getTitle();
        String phaseTitle = ((Phase)session.getAttribute("phase")).getTitle();
        String assignmentTitle = ((Assignment)session.getAttribute("assignment")).getTitle();

        Task task = projectCreator.createTask(assignmentTitle);
        session.setAttribute("task",task);
        return "redirect:/project_page-" + projectTitle + "/" + phaseTitle + "/" + phaseTitle + "/" + task.getTitle();
    }

    @PostMapping("/update_task")
    public String updateTask(@RequestParam(name="new_title") String newTitle,
                                   @RequestParam(name="task_start") String taskStart,
                                   @RequestParam(name="task_end") String taskEnd,
                                   @RequestParam(name="work_hours") String workHours,
                                   HttpServletRequest request,Model model) {

        HttpSession session = request.getSession();
        String projectTitle = ((Project)session.getAttribute("project")).getTitle();
        String phaseTitle = ((Phase)session.getAttribute("phase")).getTitle();
        String assignmentTitle = ((Assignment)session.getAttribute("assignment")).getTitle();
        Task task = (Task)session.getAttribute("task");

            String exception = handler.isLengthAllowedInDatabase(newTitle,"task_title");
            if (!exception.equals("Input is allowed")) {
                model.addAttribute("Exception",exception);
                return "redirect:/project_page-" + projectTitle  + "/" + phaseTitle + "/" + assignmentTitle + "/" +
                        task.getTitle() + "+" + task.getStart() + "+" + task.getEnd();
            }

            if (handler.isDateTimeCorrectFormat(taskStart)) {
                model.addAttribute("Exception","Start should have the format yyyy-mm-dd hh-mm-dd");
                return "redirect:/project_page-" + projectTitle  + "/" + phaseTitle + "/" + assignmentTitle + "/" +
                        task.getTitle() + "+" + task.getStart() + "+" + task.getEnd();
            }

            if (handler.isDateTimeCorrectFormat("Start should have the format yyyy-mm-dd hh-mm-dd")) {
                model.addAttribute("Exception","Start should have the format yyyy-mm-dd hh-mm-dd");
                return "redirect:/project_page-" + projectTitle  + "/" + phaseTitle + "/" + assignmentTitle + "/" +
                        task.getTitle() + "+" + task.getStart() + "+" + task.getEnd();
            }

            task = projectEditor.updateTask(newTitle,taskStart,taskEnd,Double.parseDouble(workHours), task.getTitle(),assignmentTitle);

            if (handler.doesTaskExist(task.getTitle(),task.getStart(),taskEnd)) {
                model.addAttribute("Exception","Task already exists");
                return "redirect:/project_page-" + projectTitle  + "/" + phaseTitle + "/" + assignmentTitle + "/" +
                        task.getTitle() + "+" + task.getStart() + "+" + task.getEnd();
            }
            session.setAttribute("task",task);
            return "redirect:/project_page-" + projectTitle + "/" + phaseTitle + "/" + assignmentTitle + "\" " + task.getTitle() + "+" + task.getStart() + "+" + task.getEnd();

    }

    @PostMapping("/change_task_is_completed_status")
    public String changeTaskIscompletedStatus(HttpServletRequest request) {

        HttpSession session = request.getSession();
        Task task = (Task) session.getAttribute("task");
        String phaseTitle = ((Phase)session.getAttribute("phase")).getTitle();

        if (task.isCompleted()) {
            projectEditor.changeIsCompletedTask(false,task.getTitle(), task.getStart(),task.getEnd(),
                                                ((Phase)session.getAttribute("phase")).getTitle());
        }
        else {
            projectEditor.changeIsCompletedTask(true,task.getTitle(), task.getStart(),task.getEnd(),
                    ((Phase)session.getAttribute("phase")).getTitle());
        }

        Assignment assignment = (Assignment) session.getAttribute("assignment");

        boolean allTaskAreComplete = false;
        for (int i = 0; i < assignment.getTasks().size(); i++) {
            if (!assignment.getTasks().get(i).isCompleted()) {
                allTaskAreComplete = true;
                break;
            }
        }
        if (allTaskAreComplete) {
            projectEditor.changeIsCompletedAssignment(true,assignment.getTitle(),phaseTitle);
        }

        // TODO Where should this go?
        return "redirect:/project_page-" + ((Project)session.getAttribute("project")).getTitle() + "/" + phaseTitle + "/" + task.getTitle() + "+" +
                task.getStart() + "+" + task.getEnd();
    }

    @PostMapping("/direct_to_task")
    public String directToTask(@RequestParam(name="task_title") String taskTitle, HttpServletRequest request) {
        HttpSession session = request.getSession();
        return "redirect:/project_page-" + ((Project)session.getAttribute("project")).getTitle() + "/" + taskTitle;
    }

    @GetMapping("/projectpage-{project.getTitle()}/{phase.getTitle()}/{assignment.getTitle()}/{task.getTitle()}+{task.getStart()}+{task.getEnd()}")
    public String renderTask(@PathVariable(name = "project.getTitle()") String projectTitle,
                                   @PathVariable(name = "phase.getTitle()") String phaseTitle,
                                   @PathVariable(name = "assignment.getTitle()") String assignmentTitle,
                                    @PathVariable(name = "task.getTitle()") String taskTitle,
                                    @PathVariable(name = "task.getStart()") String taskStart,
                                    @PathVariable(name = "task.getEnd()") String taskEnd,
                                    Model model,HttpServletRequest request) {

        HttpSession session = request.getSession();

        session.setAttribute("task",projectCreator.getTask(taskTitle,taskStart,taskEnd));

        model.addAttribute("project",projectCreator.getProject(projectTitle));
        model.addAttribute("phase",projectCreator.getPhase(phaseTitle,projectTitle));
        model.addAttribute("assignment",projectCreator.getAssignment(assignmentTitle, projectTitle));
        model.addAttribute("task",projectCreator.getAssignment(assignmentTitle, projectTitle));

        return "assignment";
    }

    @GetMapping("/accept_delete_of_{task.getTitle()}")
    public String renderDeleteTask(Model model,HttpServletRequest request) {
        HttpSession session = request.getSession();
        model.addAttribute("Object_to_delete",((Task)session.getAttribute("Task")));
        return "accept_delete";
    }

    @PostMapping("/delete_task")
    public String deleteTask(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String projectTitle = ((Project)session.getAttribute("project")).getTitle();
        String phaseTitle = ((Phase)session.getAttribute("phase")).getTitle();
        String assignmentTitle = ((Assignment)session.getAttribute("assignment")).getTitle();
        projectEditor.deleteTask(((Task)session.getAttribute("task")).getTitle(),assignmentTitle);
        return "redirect:/project_page-" + projectTitle + "/" + phaseTitle + "/" + assignmentTitle;
    }
}
