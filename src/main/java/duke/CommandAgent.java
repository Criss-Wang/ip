package duke;

import java.util.List;

import duke.command.Command;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.TaskList;
import duke.task.Todo;

/**
 * An agent to process incoming duke.command and return the feedback.
 */
public class CommandAgent {
    private static TaskList taskList = new TaskList();

    public static int listSize() {
        return taskList.getSize();
    }
    /**
     * Takes in the duke.command and handle it based on the request from the duke.command.
     *
     * @param command the duke.command parsed from user input.
     * @return a String feedback for the user.
     */
    public String handleCommand(Command command) {
        String feedback = "____________________________________________________________\n";
        switch (command.sendRequest()) {
        case "error":
            List<String> errorMessage = command.getContent();
            feedback += errorMessage.get(0);
            break;
        case "end":
            feedback += "Bye. Hope to see you again soon!";
            break;
        case "create":
            List<String> taskInfo = command.getContent();
            Task newTask = this.createTask(taskInfo);
            taskList = taskList.addTask(newTask);
            feedback += this.generateCreateFeedback();
            break;
        case "retrieval":
            feedback += this.generateRetrievalFeedback();
            break;
        case "update":
            List<String> contentList = command.getContent();
            int taskId = Integer.parseInt(contentList.get(0));
            taskList = taskList.markAsDone(taskId);
            feedback += this.generateUpdateFeedback(taskId);
            break;
        default:
            feedback += "";
        }
        feedback += "\n____________________________________________________________\n";
        return feedback;
    }

    /**
     * Create different types of tasks based on the identifier stored in the taskInfo.
     *
     * @param taskInfo a list of String containing all the relevant information for the duke.task.
     * @return a correct type of Task object.
     */
    public Task createTask(List<String> taskInfo) {
        String identifier = taskInfo.get(0);
        String name = taskInfo.get(1);
        String schedule;
        switch (identifier) {
        case "E":
            schedule = taskInfo.get(2);
            return new Event(name, false, schedule);
        case "T":
            return new Todo(name, false);
        case "D":
            schedule = taskInfo.get(2);
            return new Deadline(name, false, schedule);
        default:
            return new Task(name);
        }
    }

    /**
     * Generate the feedback for a duke.task creation.
     * @return a String suggesting the completion of duke.task creation.
     */
    public String generateCreateFeedback() {
        int taskId = taskList.getSize();
        Task currentTask = taskList.getTaskById(taskId);
        String result = "Got it. I've added this duke.task:\n  ";
        result += currentTask;
        result += String.format("\nNow you have %d tasks in the list.", taskId);
        return result;
    }

    /**
     * Generate the feedback for an update of duke.task status.
     *
     * @param taskId the displayed id in the taskList.
     * @return a String suggesting the completion of duke.task update.
     */
    public String generateUpdateFeedback(int taskId) {
        Task currentTask = taskList.getTaskById(taskId);
        return "Nice! I've marked this duke.task as done:\n  " + currentTask;
    }

    public String generateRetrievalFeedback() {
        return taskList.printTasks();
    }
}