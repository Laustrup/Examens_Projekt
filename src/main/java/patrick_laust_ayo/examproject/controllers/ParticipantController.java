package patrick_laust_ayo.examproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import patrick_laust_ayo.examproject.models.Participant;
import patrick_laust_ayo.examproject.models.Project;
import patrick_laust_ayo.examproject.models.ProjectManager;
import patrick_laust_ayo.examproject.repositories.DepartmentRepository;
import patrick_laust_ayo.examproject.services.ExceptionHandler;
import patrick_laust_ayo.examproject.services.ProjectCreator;
import patrick_laust_ayo.examproject.services.UserCreator;
import patrick_laust_ayo.examproject.services.UserEditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;


@Controller
public class ParticipantController {

    private UserCreator userCreator = new UserCreator();
    private UserEditor userEditor = new UserEditor();

    @GetMapping("/participant_login_page")
    public String renderLoginParticipant(HttpServletRequest request, Model model){

        HttpSession session = request.getSession();
        model.addAttribute("Exception", session.getAttribute("userIdOrPasswordException"));
        if (session.getAttribute("current_login")==null || session.getAttribute("current_login").equals("without_invite")) {
            model.addAttribute("current_login","without_invite");
        }
        else {
            model.addAttribute("current_login","with_invite");
        }
        session.setAttribute("current_login","without_invite");
        return "participant_login";
    }

    @PostMapping("/login_to_participant_dashboard")
    public String checkLoginToDashboard(@RequestParam (name="participant_id") String userId,
                             @RequestParam (name="participant_password") String password,
                             HttpServletRequest request, Model model){

        HttpSession session = request.getSession();
        ExceptionHandler exceptionHandler = new ExceptionHandler();

        if (exceptionHandler.allowLogin(userId, password)){
            session.setAttribute("participant", userCreator.getParticipant(userId));
            return "redirect:/participant_dashboard/" + userId;
        }

        model.addAttribute("Exception","Wrong user-id or password!");
        return "redirect:/participant_login_page";
    }

    @GetMapping("/add_participant/projectmanager/{project_title}")
    public String addParticipant(@PathVariable(name = "project_title") String projectTitle,
                                 Model model,HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("current", "start");
        model.addAttribute("project_title", projectTitle);
        return "add_projectmanager_as_participant";
    }

    @PostMapping("/projectmanager/participant_added")
    public String participantAdded(@RequestParam(name = "project_title") String projectTitle,
                                   @RequestParam(name = "department_name") String depName,
                                   HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        ProjectManager projectManager = (ProjectManager) session.getAttribute("projectManager");
        String projectManagerPassword = (String) session.getAttribute("projectmanager_password");

        userCreator.createProjectManagerAsParticipant(projectManager.getUsername(), depName, projectTitle);

        session.setAttribute("participant", userEditor.updateParticipant(projectManager.getUsername(),
                projectManagerPassword, "null", "null", depName,
                projectManager.getUsername(), true));

        session.setAttribute("current_project", "start");
        model.addAttribute("current_project", session.getAttribute("current_project"));

        return "redirect:/project_page-" + projectTitle + "/" + projectManager.getUsername();
    }

    @GetMapping("/participant_dashboard/{participant_user_id}")
    public String renderDashboard(@PathVariable (name="participant_user_id") String userId, Model model){

        model.addAttribute("projects", new ProjectCreator().getProjects(userId));
        model.addAttribute("participant", new UserCreator().getParticipant(userId));
        model.addAttribute("current_user","participant");

        return "dashboard";
    }

    @PostMapping("/go_to_projectpage")
    public String EnterProject(@RequestParam (name="project_title") String projectTitle,
                               @RequestParam (name="user_id") String userId,
                               Model model,HttpServletRequest request){

        HttpSession session = request.getSession();

        Project project = new ProjectCreator().getProject(projectTitle);

        session.setAttribute("project", project);
        model.addAttribute("project", project);
        model.addAttribute("participant", new UserCreator().getParticipant(userId));

        return "/projectpage/" + projectTitle + "/" + userId;
    }

    @PostMapping("/join-project")
    public String joinProject(@RequestParam(name="participant_id") String id,
                              @RequestParam(name="participant_password") String password,
                              HttpServletRequest request) {

        ExceptionHandler handler = new ExceptionHandler();
        HttpSession session = request.getSession();
        Project project = (Project) session.getAttribute("project");

        if (handler.allowLogin(id, password)) {
            Participant participant = userCreator.getParticipant(id);
            session.setAttribute("participant", participant);

            if (new UserEditor().joinParticipantToProject(participant,project).equals("Project is fully booked, projectmanager needs to add more participants of your department...")) {
                session.setAttribute("Exception", "Project is fully booked of participant of that department. " +
                        "Contact your Projectmanager to gain access to it.");
                return "redirect:/participant_login_page";
            }
            else {
                return "redirect:/projectpage-" + project.getTitle() + "/" + participant.getId();
            }
        }
        else {
                session.setAttribute("userIdOrPasswordException", "Wrong user-id or password!");
            return "redirect:/participant_login_page";
        }
    }

    @PostMapping("/join_project_as_new_participant")
    public String newParticipantJoinProject(@RequestParam(name = "participant_ID") String userId,
                                            @RequestParam(name = "participant_password") String password,
                                            @RequestParam(name = "participant_name") String name,
                                            @RequestParam(name = "position") String position,
                                            @RequestParam(name = "department") String departmentName,
                                            HttpServletRequest request) {

        HttpSession session = request.getSession();

        Participant participant = new Participant(userId,password,name,position,userCreator.getDepartment(departmentName));
        Project project = (Project) session.getAttribute("project");

        if (new UserEditor().joinParticipantToProject(participant,project).equals("Project is fully booked, projectmanager needs to add more participants of your department...")) {
            session.setAttribute("Exception", "Project is fully booked of participant of that department. " +
                                                    "Contact your Projectmanager to gain access to it.");
            return "redirect:/participant_login_page";
        }
        else {
            session.setAttribute("participant",participant);
            session.setAttribute("current_project","start");
            session.setAttribute("current","phases");
            return "redirect:/project_page-" + project.getTitle() + "/" + participant.getId();
        }

    }

    @PostMapping("/update_participant")
    public String updateParticipant(@RequestParam(name="participant_id") String id, @RequestParam(name="participant_password") String password,
                                    @RequestParam(name="name") String name, @RequestParam(name="position") String position,
                                    @RequestParam(name="department_name") String departmentName, HttpServletRequest request, Model model){

        ExceptionHandler handler = new ExceptionHandler();

        HttpSession session = request.getSession();
        Project project = (Project) session.getAttribute("project");

        if (handler.doesUserIdExist(id)) {
            model.addAttribute("User_id exists","User id already exists, please write another.");

            return "redirect:/project/" + project.getTitle() + "/User id already exists, please write another.";
        }

        String inputException = handler.isLengthAllowedInDatabase(id,"user_id");

        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "redirect:/project/" + project.getTitle() + "/" + inputException;
        }

        inputException = handler.isLengthAllowedInDatabase(password,"participant_password");

        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "redirect:/project/" + project.getTitle() + "/" + inputException;
        }

        inputException = handler.isLengthAllowedInDatabase(name,"pariticipant_name");

        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "redirect:/project/" + project.getTitle() + "/" + inputException;
        }

        inputException = handler.isLengthAllowedInDatabase(position,"position");

        if (!(inputException.equals("Input is allowed"))) {
            model.addAttribute("Exception",inputException);
            return "redirect:/project/" + project.getTitle() + "/" + inputException;
        }

        if (session.getAttribute("projectManager")==null) {
            session.setAttribute("participant", userEditor.updateParticipant(id, password, name, position, departmentName,
                    ((Participant)session.getAttribute("participant")).getId(),false));
        }
        else {
            session.setAttribute("participant", userEditor.updateParticipant(id, password, name, position, departmentName,
                    ((Participant)session.getAttribute("participant")).getId(),true));
            session.setAttribute("projectManager",userCreator.getProjectManager(id));
        }

        return "redirect:/participant_dashboard/" + id;

    }

    @GetMapping("/accept_delete_of_participant")
    public String renderDeletePage(Model model,HttpServletRequest request) {
        HttpSession session = request.getSession();
        model.addAttribute("Object_to_delete",((Participant)session.getAttribute("participant")).getId());
        model.addAttribute("current_delete", "participant");
        model.addAttribute("Exception","");
        model.addAttribute("Message","");
        return "accept_delete";
    }

    // TODO Add password, need html
    @PostMapping("/delete_participant")
    public String removeParticipant(@RequestParam(name = "user_id") String userId,HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        userEditor.removeParticipant(userId);
        if (session.getAttribute("projectManager")!=null) {
            userEditor.removeProjcetManager(userId);
        }
        model.addAttribute("Message","User is removed");
        return "/";
    }

    // TODO Needs task html
    @PostMapping("/join-task")
    public String joinTask(@RequestParam(name = "task_title") String taskTitle,
                           @RequestParam(name = "task_start") String taskStart,
                           @RequestParam(name = "task_end") String taskEnd,
                           HttpServletRequest request) {

        HttpSession session = request.getSession();
        Participant participant = (Participant) session.getAttribute("participant");
        String exception = userEditor.joinParticipantToTask(participant.getId(),new ProjectCreator().getTask(taskTitle, taskStart, taskEnd));
        session.setAttribute("Exception",exception+taskTitle+"!");

        if (exception.equals("You are now added to the task!")) {
            return "/projectpage-" + taskTitle + "/" + exception;
        }
        return "/task/" + taskTitle + "/" + exception;
    }

    @PostMapping("/disjoin-task")
    public String disjoinTask(@RequestParam(name = "task_title") String taskTitle,
                           @RequestParam(name = "task_start") String taskStart,
                           @RequestParam(name = "task_end") String taskEnd,
                           HttpServletRequest request) {

        HttpSession session = request.getSession();
        Participant participant = (Participant) session.getAttribute("participant");
        String exception = userEditor.joinParticipantToTask(participant.getId(),new ProjectCreator().getTask(taskTitle, taskStart, taskEnd));
        session.setAttribute("Exception",exception+taskTitle+"!");
        if (exception.equals("You are now removed from task!")) {
            return "/projectpage-" + taskTitle + "/" + exception;
        }
        return "/task/" + taskTitle + "/" + exception;
    }

    // TODO Already exist in projectcontroller?
    @GetMapping("/projectpage-{project.getTitle()}/{Exception}")
    public String projectWithException(@PathVariable(name = "Exception") String exception,
                                       HttpServletRequest request,Model model) {

        HttpSession session = request.getSession();

        model.addAttribute("Exception",exception);
        model.addAttribute("project",(Project) session.getAttribute("project"));
        model.addAttribute("participant",(Participant) session.getAttribute("participant"));

        return "project_page";
    }

}