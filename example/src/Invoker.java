import java.util.ArrayList;
import java.util.List;

public class Invoker {
	public List<Command> commandList=new ArrayList<Command>();
	
	public void takeCommand(Command command) {
		commandList.add(command);
		command.execute();
	}
	
	
}
