package patrick_laust_ayo.examproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import patrick_laust_ayo.examproject.models.*;
import patrick_laust_ayo.examproject.repositories.ProjectRepository;
import patrick_laust_ayo.examproject.services.ExceptionHandler;
import patrick_laust_ayo.examproject.services.ProjectCreator;
import patrick_laust_ayo.examproject.services.ProjectEditor;
import patrick_laust_ayo.examproject.services.UserCreator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ProjectController {

    private ProjectCreator projectCreator = new ProjectCreator();
    private ProjectEditor projectEditor = new ProjectEditor();
    private ExceptionHandler handler = new ExceptionHandler();


    @GetMapping("/create_project/{projectmanager_username}")
    public String renderCreateProject(@PathVariable(name = "projectmanager_username") String username) {
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

        return "redirect:/add_participant/projectmanager/" + title;

    }

    @PostMapping("/direct_project_page")
    public String goToChoosenProjectPage(@RequestParam(name = "projectTitle") String title, Model model, HttpServletRequest request) {
        ProjectRepository projectRepository = new ProjectRepository();
        HttpSession session = request.getSession();

        model.addAttribute("project",projectRepository.findProject(title));
        model.addAttribute("participant",session.getAttribute("participant"));

        return "redirect:/project_page/" + title + "/" + ((Participant) session.getAttribute("participant")).getId();
    }


    // TODO Does all three cases work with this endpoint? Redirect needs html
    @GetMapping("/project_page/{project_title}/{user_id}")
    public String renderProjectpage(@PathVariable(name = "project_title") String projectTitle,
                                    @PathVariable(name = "user_id") String userId,
                                    HttpServletRequest request,Model model) {

        HttpSession session = request.getSession();
        UserCreator userCreator = new UserCreator();
        // Aldrig logget ind
        if (userId == null) {
            // Needs create participant method in participant controller
        }
        //TODO projekt titlen skal være korrekt, fra en attribut/variabel
        // Får direkte adgang da han er del af projectet
        else if (handler.isParticipantPartOfProject(userId, projectTitle)) {

            Project project = new ProjectCreator().getProject(projectTitle);

            session.setAttribute("project",project);
            model.addAttribute("project",project);
            model.addAttribute("participant",new UserCreator().getParticipant(userId));

            return "project_page";
        }

        //TODO der skal være det rigtige redirect
        model.addAttribute("Exception","You are not a participant of this project...");
        return "redirect:/";
        // return "redirect:/login_to_project/" + ((Participant) session.getAttribute("participant")).getId()+ "/" + projectTitle;
    }

    // TODO Create html
    @GetMapping("/accept_delete_of_{project.getTitle()}")
    public String renderDeleteProject(Model model,HttpServletRequest request) {
        HttpSession session = request.getSession();
        model.addAttribute("Object_to_delete",((Project)session.getAttribute("Project")));
        return "accept_delete";
    }

    @PostMapping("/delete_project")
    public String deleteProject(@RequestParam(name = "project_title") String projectTitle,
                                @RequestParam(name = "password") String password,
                                @RequestParam(name = "user_id") String userId, Model model) {

        if (handler.allowLogin(userId, password)) {
            projectEditor.deleteProject(projectTitle);
            model.addAttribute("message","Project is now deleted");
            return "/" + userId;
        }

        model.addAttribute("message","Project manager is now deleted");
        return "/accept_delete_of_" + projectTitle;
    }

    @PostMapping("/add_phase}")
    public String addPhase(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();

        String projectTitle = ((Project)session.getAttribute("project")).getTitle();

        Phase phase = projectCreator.createPhase(projectTitle);

        return "/projectpage-" + ((Project)session.getAttribute("project")).getTitle() + "/" + phase.getTitle();
    }

    @PostMapping("/update_phase")
    public String updatePhase(@RequestParam(name="new_title") String newTitle, HttpServletRequest request,Model model) {

        HttpSession session = request.getSession();
        String projectTitle = ((Project)session.getAttribute("project")).getTitle();
        String phaseTitle = ((Phase)session.getAttribute("phase")).getTitle();

        String exception = handler.isLengthAllowedInDatabase(newTitle, "phase_title");
        if (exception.equals("Input is allowed")) {

            if (handler.doesPhaseExist(newTitle,phaseTitle)) {
                model.addAttribute("Exception","Phase already exists...");
                return "projectpage-" + projectTitle + "/" + phaseTitle;
            }
            Phase phase = projectEditor.updatePhase(newTitle, phaseTitle, projectTitle);
            session.setAttribute("phase",phase);
            return "projectpage-" + projectTitle + "/" + phase.getTitle();
        }

        model.addAttribute("Exception", exception);
        return "projectpage-" + projectTitle + "/" + phaseTitle;

    }

    // TODO Perhaps make submitvalue with both title and split in method?
    @PostMapping("/direct_to_phase")
    public String directToPhase(@RequestParam(name="phase_title") String phaseTitle, HttpServletRequest request) {

        HttpSession session = request.getSession();
        String projectTitle = ((Project)session.getAttribute("project")).getTitle();

        return "/projectpage-" + projectTitle + "/" + phaseTitle;
    }

    // TODO Create phase html
    @GetMapping("/projectpage-{project.getTitle()}/{phase.getTitle()}")
    public String renderPhase(@PathVariable(name = "project.getTitle()") String projectTitle,
                              @PathVariable(name = "phase.getTitle()") String phaseTitle,
                              HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();
        session.setAttribute("phase",projectCreator.getPhase(phaseTitle,projectTitle));

        model.addAttribute("project",projectCreator.getProject(projectTitle));
        model.addAttribute("phase",projectCreator.getPhase(phaseTitle,projectTitle));

        return "phase";
    }

    // TODO Create html
    @GetMapping("/accept_delete_of_{phase.getTitle()}")
    public String renderDeletePhase(Model model,HttpServletRequest request) {
        HttpSession session = request.getSession();
        model.addAttribute("Object_to_delete",((Phase)session.getAttribute("Phase")));
        return "accept_delete";
    }

    @PostMapping("/delete_phase")
    public String deletePhase(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String projectTitle = ((Project)session.getAttribute("project")).getTitle();
        projectEditor.deletePhase(((Phase)session.getAttribute("Phase")).getTitle(),projectTitle);
        return "/projectpage-"+projectTitle+"/" + ((Participant)session.getAttribute("participant")).getId();
    }

    @PostMapping("/add_assignment")
    public String addAssignment(@RequestParam(name="title") String title,
                                @RequestParam(name="start") String start,
                                @RequestParam(name="end") String end,
                                HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();

        String projectTitle = ((Project)session.getAttribute("project")).getTitle();
        String phaseTitle = ((Phase)session.getAttribute("phase")).getTitle();

        String exception = handler.isLengthAllowedInDatabase(title,"assignment_title");
        if (!exception.equals("Input is allowed")) {
            model.addAttribute("Exception",exception);
            return "/projectpage-" + projectTitle  + "/" + phaseTitle;
        }

        if (handler.isDateTimeCorrectFormat(start)) {
            model.addAttribute("Exception","Start should have the format yyyy-mm-dd hh-mm-dd");
            return "/projectpage-" + projectTitle  + "/" + phaseTitle;
        }

        if (handler.isDateTimeCorrectFormat(end)) {
            model.addAttribute("Exception","End should have the format yyyy-mm-dd hh-mm-dd");
            return "/projectpage-" + projectTitle  + "/" + phaseTitle;
        }

        Assignment assignment = projectCreator.createAssignment(title,start,end);

        return "/projectpage-" + projectTitle + "/" + phaseTitle + "/" + assignment.getTitle();
    }

    @PostMapping("/update_assignment")
    public String updateAssignment(@RequestParam(name="new_title") String newTitle,
                                   @RequestParam(name="start") String start,
                                   @RequestParam(name="end") String end,
                                   HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();
        String projectTitle = ((Project) session.getAttribute("project")).getTitle();
        String phaseTitle = ((Phase) session.getAttribute("phase")).getTitle();
        String assignmentTitle = ((Assignment) session.getAttribute("assignment")).getTitle();

            String exception = handler.isLengthAllowedInDatabase(newTitle, "assignment_title");
            if (!exception.equals("Input is allowed")) {
                model.addAttribute("Exception", exception);
                return "/projectpage-" + projectTitle + "/" + phaseTitle + "/" + assignmentTitle;
            }

            if (handler.isDateTimeCorrectFormat(start)) {
                model.addAttribute("Exception", "Start should have the format yyyy-mm-dd hh-mm-dd");
                return "/projectpage-" + projectTitle + "/" + phaseTitle + "/" + assignmentTitle;
            }

            if (handler.isDateTimeCorrectFormat(end)) {
                model.addAttribute("Exception", "End should have the format yyyy-mm-dd hh-mm-dd");
                return "/projectpage-" + projectTitle + "/" + phaseTitle + "/" + assignmentTitle;
            }

            Assignment assignment = projectEditor.updateAssignment(newTitle, start, end, assignmentTitle, phaseTitle);

            if (handler.doesAssignmentExist(assignment.getTitle(),phaseTitle)) {
                model.addAttribute("Exception","Assignment already exists");
                return "/projectpage-" + projectTitle + "/" + phaseTitle + "/" + assignmentTitle;
            }
            session.setAttribute("assignment", assignment);

            return "/projectpage-" + projectTitle + "/" + phaseTitle + "/" + assignment.getTitle();

    }

    @PostMapping("/change_assignment_is_completed_status")
    public String changeAssignmentIscompletedStatus(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Assignment assignment = projectCreator.getAssignment(((Assignment)session.getAttribute("assignment")).getTitle(),
                                                            ((Phase)session.getAttribute("phase")).getTitle());
        if (assignment.isCompleted()) {
            projectEditor.changeIsCompletedAssignment(false,assignment.getTitle(),((Phase)session.getAttribute("phase")).getTitle());
        }
        else {
            projectEditor.changeIsCompletedAssignment(true,assignment.getTitle(),((Phase)session.getAttribute("phase")).getTitle());
        }

        return "/projectpage-" + ((Project)session.getAttribute("project")).getTitle() + "/" +
                ((Phase)session.getAttribute("phase")).getTitle() + "/" + assignment.getTitle();
    }

    @PostMapping("/direct_to_assignment")
    public String directToAssignment(@RequestParam(name="assignment_title") String assignmentTitle, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("assignment",projectCreator.getAssignment(assignmentTitle,((Phase)session.getAttribute("phase")).getTitle()));
        return "/projectpage-" + ((Project)session.getAttribute("project")).getTitle() + "/" +
                ((Phase)session.getAttribute("phase")).getTitle() + "/" +  assignmentTitle;
    }

    // TODO Create assignment html
    @GetMapping("/projectpage-{project.getTitle()}/{phase.getTitle()}/{assignment.getTitle()}")
    public String renderAssignment(@PathVariable(name = "project.getTitle()") String projectTitle,
                                   @PathVariable(name = "phase.getTitle()") String phaseTitle,
                                   @PathVariable(name = "assignment.getTitle()") String assignmentTitle,
                                   HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();
        session.setAttribute("assignment",projectCreator.getAssignment(assignmentTitle,phaseTitle));

        model.addAttribute("project",projectCreator.getProject(projectTitle));
        model.addAttribute("phase",projectCreator.getPhase(phaseTitle,projectTitle));
        model.addAttribute("assignment",projectCreator.getAssignment(assignmentTitle, projectTitle));

        return "assignment";
    }

    // TODO Create html
    @GetMapping("/accept_delete_of_{assignment.getTitle()}")
    public String renderDeleteAssignment(Model model,HttpServletRequest request) {
        HttpSession session = request.getSession();
        model.addAttribute("Object_to_delete",((Assignment)session.getAttribute("Assignment")));
        return "accept_delete";
    }

    @PostMapping("/delete_assignment")
    public String deleteAssignment(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String projectTitle = ((Project)session.getAttribute("project")).getTitle();
        String phaseTitle = ((Phase)session.getAttribute("phase")).getTitle();
        projectEditor.deleteAssignment(((Assignment)session.getAttribute("assignment")).getTitle(),phaseTitle);
        return "/projectpage-" + projectTitle + "/" + phaseTitle;
    }

    @PostMapping("/add_task")
    public String addTask(HttpServletRequest request) {

        HttpSession session = request.getSession();

        String projectTitle = ((Project)session.getAttribute("project")).getTitle();
        String phaseTitle = ((Phase)session.getAttribute("phase")).getTitle();
        String assignmentTitle = ((Assignment)session.getAttribute("assignment")).getTitle();

        Task task = projectCreator.createTask(assignmentTitle);
        session.setAttribute("task",task);
        return "/projectpage-" + projectTitle + "/" + phaseTitle + "/" + phaseTitle + "/" + task.getTitle();
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
                return "/projectpage-" + projectTitle  + "/" + phaseTitle + "/" + assignmentTitle + "/" +
                        task.getTitle() + "+" + task.getStart() + "+" + task.getEnd();
            }

            if (handler.isDateTimeCorrectFormat(taskStart)) {
                model.addAttribute("Exception","Start should have the format yyyy-mm-dd hh-mm-dd");
                return "/projectpage-" + projectTitle  + "/" + phaseTitle + "/" + assignmentTitle + "/" +
                        task.getTitle() + "+" + task.getStart() + "+" + task.getEnd();
            }

            if (handler.isDateTimeCorrectFormat("Start should have the format yyyy-mm-dd hh-mm-dd")) {
                model.addAttribute("Exception","Start should have the format yyyy-mm-dd hh-mm-dd");
                return "/projectpage-" + projectTitle  + "/" + phaseTitle + "/" + assignmentTitle + "/" +
                        task.getTitle() + "+" + task.getStart() + "+" + task.getEnd();
            }

            task = projectEditor.updateTask(newTitle,taskStart,taskEnd,Double.parseDouble(workHours), task.getTitle(),assignmentTitle);

            if (handler.doesTaskExist(task.getTitle(),task.getStart(),taskEnd)) {
                model.addAttribute("Exception","Task already exists");
                return "/projectpage-" + projectTitle  + "/" + phaseTitle + "/" + assignmentTitle + "/" +
                        task.getTitle() + "+" + task.getStart() + "+" + task.getEnd();
            }
            session.setAttribute("task",task);
            return "projectpage-" + projectTitle + "/" + phaseTitle + "/" + assignmentTitle + "\" " + task.getTitle() + "+" + task.getStart() + "+" + task.getEnd();

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
        return "/projectpage-" + ((Project)session.getAttribute("project")).getTitle() + "/" + phaseTitle + "/" + task.getTitle() + "+" +
                task.getStart() + "+" + task.getEnd();
    }

    // TODO how to get three task variables as RequestParam?
    @PostMapping("/direct_to_task")
    public String directToTask(@RequestParam(name="task_title") String taskTitle, HttpServletRequest request) {
        HttpSession session = request.getSession();
        return "/projectpage-" + ((Project)session.getAttribute("project")).getTitle() + "/" + taskTitle;
    }

    // TODO Create assignment html
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

    // TODO Create html
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
        return "/projectpage-" + projectTitle + "/" + phaseTitle + "/" + assignmentTitle;
    }

}
