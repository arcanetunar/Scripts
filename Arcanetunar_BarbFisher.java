import java.util.*;
import javax.swing.*;

import org.powerbot.core.script.*;
import org.powerbot.core.script.job.*;
import org.powerbot.core.script.job.state.*;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.*;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.*;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.interactive.NPC;

@Manifest(authors = { "arcanetunar" }, description = "Powerfishes @barb village", name = "ArcaneTunar_BarbFisher", version = 0.1)
public class Arcanetunar_BarbFisher extends ActiveScript {

  public static int bait = 314;
	public int spot = 328;
	public static int salmon = 331;
	public static int trout = 335;
	
	
	private final List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());
	private Tree jobContainer = null;
	
	public synchronized final void provide(final Node... jobs) {
		for (final Node job : jobs) {
			if (!jobsCollection.contains(job)) {
				jobsCollection.add(job);
			}
		}
		jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
	}

	public synchronized final void revoke(final Node... jobs) {
		for (final Node job : jobs) {
			if (jobsCollection.contains(job)) {
				jobsCollection.remove(job);
			}
		}
		jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection.size()]));
	}

	public final void submit(final Job... jobs) {
		for (final Job job : jobs) {
			getContainer().submit(job);
		}
	}
	
	@Override
	public void onStart() {
		provide(new actionb(), new feathers(), new fish(), new drop());
	}
	
	@Override
	public int loop() {
		if (jobContainer != null){
			final Node job = jobContainer.state();
			if (job != null) {
				jobContainer.set(job);
				getContainer().submit(job);
				job.join();
			}
		}
		return Random.nextInt(10, 25);
	}
	
	public class actionb extends Node {

		@Override
		public boolean activate() {
			return Widgets.get(640, 3).visible();
		}

		@Override
		public void execute() {
			if (Widgets.get(640, 3).visible()) {
				Widgets.get(640, 3).interact("expand");
			}

			
		}
	}
	
	public class feathers extends Node {

		@Override
		public boolean activate() {
			return Inventory.getCount(bait) == 0;
		}

		@Override
		public void execute() {
			if (Inventory.getCount(314) == 0) {
				Tabs.LOGOUT.open();
				if (Tabs.LOGOUT.isOpen()) {
				Game.logout(true);
				showMessage("No feathers in inventory, script stopped!");
				stop();
				}
				}

			
		}
			
			 private void showMessage(String txt) {
JOptionPane.showMessageDialog(null, txt);
}
	}
	
	public class fish extends Node {

		@Override
		public boolean activate() {
			 return Players.getLocal().isIdle() && !Inventory.isFull();
		}

		@Override
		public void execute() {
			NPC fishspot = NPCs.getNearest(spot);
            if(fishspot != null) {
                    if(Players.getLocal().isIdle()) {
                    if(fishspot.isOnScreen()) {
                            fishspot.interact("Lure");
                            Task.sleep(400, 1200);
                    }
                    else
                    {
                            Camera.turnTo(fishspot);
                    
                    }
            }
           
    }
   
}
		
		
	}

	public class drop extends Node {

		@Override
		public boolean activate() {
			return Inventory.getCount(trout) > 0 && Inventory.getCount(salmon) > 0;
		}

		@Override
		public void execute() {
			if (Players.getLocal().isIdle()) {
			if (Inventory.getCount(trout) > 0 && Inventory.getCount(salmon) > 0) {
				Keyboard.sendKey('1');
				Keyboard.sendKey('2');
				Keyboard.sendKey('3');
				Keyboard.sendKey('4');
				Keyboard.sendKey('5');
				Keyboard.sendKey('6');
				Keyboard.sendKey('7');
				Keyboard.sendKey('8');
				Keyboard.sendKey('9');
				Keyboard.sendKey('0');
				Keyboard.sendKey(')');
				Keyboard.sendKey('-');
			}
		}
	}
		
}
	
}
	

