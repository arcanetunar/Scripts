import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.powerbot.core.event.events.MessageEvent;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.widget.WidgetChild;



@Manifest(authors = { "arcanetunar" }, description = "Trains Fletching for you!", name = "ArcaneTunar_AIOFletcher")
public class Fletcher extends ActiveScript implements PaintListener{
  
    
   private final Color color1 = new Color(0,0,139, 50);
   private final Color color2 = new Color(0, 0, 0);
   private final Color color3 = new Color(250, 10, 10);
   private final Color color4 = new Color(183, 57, 57);
   private final Color color5 = new Color(69, 13, 207);
   private final Color color6 = new Color(244, 242, 249);
   private final BasicStroke stroke1 = new BasicStroke(1);
   private final Font font1 = new Font("Century Gothic", 0, 25);
   private final Font font2 = new Font("Century Gothic", 0, 20);
   
   private Timer timer = new Timer(0);
   private int banker = 3418;
   private int logs;
   int fletchesdone = 0;
   public int startexp;
	 
	 
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
	
	@Override
    public void onStart() {
		startexp = Skills.getExperience(Skills.FLETCHING); 
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					new GUI();
				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
		});
    }
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
	
	public class banker extends Node {

		@Override
		public boolean activate() {
			return Inventory.getCount(logs) == 0;
		}

		@Override
		public void execute() {
			NPC bank = NPCs.getNearest(banker);
			if(Bank.isOpen()){
				Bank.depositInventory();
				Task.sleep(250, 400);
				Bank.withdraw(logs, Bank.Amount.ALL);
				Bank.close();
				Task.sleep(250, 500);
			} else if (!Bank.isOpen()) {
				bank.interact("Bank");
				Task.sleep(1250);
			}
		}
		
	}
	
	 public class sbowfletcher extends Node implements MessageListener {
		
         @Override
         public boolean activate() {
                 return Inventory.getCount(logs) > 0;
         }

         @Override
         public void execute() {
        	 	 NPC bank = NPCs.getNearest(banker);
                 Item log = Inventory.getItem(logs);
                 WidgetChild shortbowu = Widgets.get(1371, 44).getChild(4);
                 WidgetChild fletch = Widgets.get(1371, 5);
                 final Timer timer = new Timer(5000);           
                         if (log.getWidgetChild().interact("Craft")) {
                        	 Task.sleep(1500);
                        	 
                                 while (!shortbowu.validate()) {
                                         Task.sleep(100);
                                         if (!timer.isRunning())
                                                 break;
                                 }
                                 if(shortbowu.validate()) {
                                         Mouse.click(shortbowu.getCentralPoint(), true);
                                 }
                                 while (!fletch.validate()) {
                                         Task.sleep(100);
                                         if (!timer.isRunning())
                                                 break;
                                 }
                                 if(fletch.validate()) {
                                         Mouse.click(fletch.getCentralPoint(), true);
                                 }
                                Task.sleep(51000);
                                bank.interact("Bank");
                 				Task.sleep(1250);
                         }
                 }
    	 @Override
         public void messageReceived(MessageEvent e) {
    					if(e.getMessage().equals("You carefully cut the logs into a shortbow."))
                                 fletchesdone++;                
    		}
         }

	 public class lbowfletcher extends Node implements MessageListener {

		@Override
		public boolean activate() {
			return Inventory.getCount(logs) > 0;
		}

		@Override
		public void execute() {
			NPC bank = NPCs.getNearest(banker);
			Item log = Inventory.getItem(logs);
			WidgetChild fletch = Widgets.get(1371, 5);
			WidgetChild lbowu = Widgets.get(1371, 44).getChild(12);
			final Timer timer = new Timer(5000);
			if (log.getWidgetChild().interact("Craft")) {
				Task.sleep(1500);
                while (!lbowu.validate()) {
                        Task.sleep(100);
                        if (!timer.isRunning())
                                break;
                }
                if(lbowu.validate()) {
                        Mouse.click(lbowu.getCentralPoint(), true);
                }
                while (!fletch.validate()) {
                        Task.sleep(100);
                        if (!timer.isRunning())
                                break;
                }
                if(fletch.validate()) {
                        Mouse.click(fletch.getCentralPoint(), true);
                }
                Task.sleep(51000);
                bank.interact("Bank");
                Task.sleep(1250);
			}
		}
		@Override
        public void messageReceived(MessageEvent e) {
   					if(e.getMessage().equals("You carefully cut the wood into a Shieldbow (u)."))
                                fletchesdone++;                
   		}
		 
	 }
	 
	 public class fletchsbu extends Node implements MessageListener {

		@Override
		public boolean activate() {
			return Inventory.getCount(logs) > 0;
		}

		@Override
		public void execute() {
			NPC bank = NPCs.getNearest(banker);
			Item log = Inventory.getItem(logs);
			WidgetChild fletch = Widgets.get(1371, 5);
			WidgetChild sbowu = Widgets.get(1371, 44).getChild(0);
			final Timer timer = new Timer(5000);
			if (log.getWidgetChild().interact("Craft")) {
				Task.sleep(1500);
                while (!sbowu.validate()) {
                        Task.sleep(100);
                        if (!timer.isRunning())
                                break;
                }
                if(sbowu.validate()) {
                        Mouse.click(sbowu.getCentralPoint(), true);
                }
                while (!fletch.validate()) {
                        Task.sleep(100);
                        if (!timer.isRunning())
                                break;
                }
                if(fletch.validate()) {
                        Mouse.click(fletch.getCentralPoint(), true);
                }
                Task.sleep(51000);
                bank.interact("Bank");
                Task.sleep(1250);
			}
		}
		@Override
        public void messageReceived(MessageEvent e) {
   					if(e.getMessage().equals("You carefully cut the wood into an Oak shortbow (u)."))
                                fletchesdone++;                
   		}
		 
	 }
	 
	 public class fletchlbu extends Node  implements MessageListener{

		@Override
		public boolean activate() {
			return Inventory.getCount(logs) > 0;
		}

		@Override
		public void execute() {
			NPC bank = NPCs.getNearest(banker);
			Item log = Inventory.getItem(logs);
			WidgetChild fletch = Widgets.get(1371, 5);
			WidgetChild lbowu = Widgets.get(1371, 44).getChild(8);
			final Timer timer = new Timer(5000);
			if (log.getWidgetChild().interact("Craft")) {
				Task.sleep(1500);
                while (!lbowu.validate()) {
                        Task.sleep(100);
                        if (!timer.isRunning())
                                break;
                }
                if(lbowu.validate()) {
                        Mouse.click(lbowu.getCentralPoint(), true);
                }
                while (!fletch.validate()) {
                        Task.sleep(100);
                        if (!timer.isRunning())
                                break;
                }
                if(fletch.validate()) {
                        Mouse.click(fletch.getCentralPoint(), true);
                }
                Task.sleep(50000);
                bank.interact("Bank");
                Task.sleep(1250);
			}
		}

		@Override
        public void messageReceived(MessageEvent e) {
   					if(e.getMessage().equals("You carefully cut the wood into an Oak shieldbow (u)."))
                                fletchesdone++;                
   		}
		 
	 }
	 
	 public class wfletchsbu extends Node implements MessageListener {

			@Override
			public boolean activate() {
				return Inventory.getCount(logs) > 0;
			}

			@Override
			public void execute() {
				NPC bank = NPCs.getNearest(banker);
				Item log = Inventory.getItem(logs);
				WidgetChild fletch = Widgets.get(1371, 5);
				WidgetChild sbowu = Widgets.get(1371, 44).getChild(0);
				final Timer timer = new Timer(5000);
				if (log.getWidgetChild().interact("Craft")) {
					Task.sleep(1500);
	                while (!sbowu.validate()) {
	                        Task.sleep(100);
	                        if (!timer.isRunning())
	                                break;
	                }
	                if(sbowu.validate()) {
	                        Mouse.click(sbowu.getCentralPoint(), true);
	                }
	                while (!fletch.validate()) {
	                        Task.sleep(100);
	                        if (!timer.isRunning())
	                                break;
	                }
	                if(fletch.validate()) {
	                        Mouse.click(fletch.getCentralPoint(), true);
	                }
	                Task.sleep(51000);
	                bank.interact("Bank");
	                Task.sleep(1250);
				}
			}
			@Override
	        public void messageReceived(MessageEvent e) {
	   					if(e.getMessage().equals("You carefully cut the wood into a Willow shortbow (u)."))
	                                fletchesdone++;                
	   		}
			 
		 }
		 
		 public class wfletchlbu extends Node  implements MessageListener{

			@Override
			public boolean activate() {
				return Inventory.getCount(logs) > 0;
			}

			@Override
			public void execute() {
				NPC bank = NPCs.getNearest(banker);
				Item log = Inventory.getItem(logs);
				WidgetChild fletch = Widgets.get(1371, 5);
				WidgetChild lbowu = Widgets.get(1371, 44).getChild(8);
				final Timer timer = new Timer(5000);
				if (log.getWidgetChild().interact("Craft")) {
					Task.sleep(1500);
	                while (!lbowu.validate()) {
	                        Task.sleep(100);
	                        if (!timer.isRunning())
	                                break;
	                }
	                if(lbowu.validate()) {
	                        Mouse.click(lbowu.getCentralPoint(), true);
	                }
	                while (!fletch.validate()) {
	                        Task.sleep(100);
	                        if (!timer.isRunning())
	                                break;
	                }
	                if(fletch.validate()) {
	                        Mouse.click(fletch.getCentralPoint(), true);
	                }
	                Task.sleep(50000);
	                bank.interact("Bank");
	                Task.sleep(1250);
				}
			}

			@Override
	        public void messageReceived(MessageEvent e) {
	   					if(e.getMessage().equals("You carefully cut the wood into a Willow shieldbow (u)."))
	                                fletchesdone++;                
	   		}
			 
		 }
		 
		 public class mfletchsbu extends Node implements MessageListener {

				@Override
				public boolean activate() {
					return Inventory.getCount(logs) > 0;
				}

				@Override
				public void execute() {
					NPC bank = NPCs.getNearest(banker);
					Item log = Inventory.getItem(logs);
					WidgetChild fletch = Widgets.get(1371, 5);
					WidgetChild sbowu = Widgets.get(1371, 44).getChild(0);
					final Timer timer = new Timer(5000);
					if (log.getWidgetChild().interact("Craft")) {
						Task.sleep(1500);
		                while (!sbowu.validate()) {
		                        Task.sleep(100);
		                        if (!timer.isRunning())
		                                break;
		                }
		                if(sbowu.validate()) {
		                        Mouse.click(sbowu.getCentralPoint(), true);
		                }
		                while (!fletch.validate()) {
		                        Task.sleep(100);
		                        if (!timer.isRunning())
		                                break;
		                }
		                if(fletch.validate()) {
		                        Mouse.click(fletch.getCentralPoint(), true);
		                }
		                Task.sleep(51000);
		                bank.interact("Bank");
		                Task.sleep(1250);
					}
				}
				@Override
		        public void messageReceived(MessageEvent e) {
		   					if(e.getMessage().equals("You carefully cut the wood into a Maple shortbow (u)."))
		                                fletchesdone++;                
		   		}
				 
			 }
			 
			 public class mfletchlbu extends Node  implements MessageListener{

				@Override
				public boolean activate() {
					return Inventory.getCount(logs) > 0;
				}

				@Override
				public void execute() {
					NPC bank = NPCs.getNearest(banker);
					Item log = Inventory.getItem(logs);
					WidgetChild fletch = Widgets.get(1371, 5);
					WidgetChild lbowu = Widgets.get(1371, 44).getChild(8);
					final Timer timer = new Timer(5000);
					if (log.getWidgetChild().interact("Craft")) {
						Task.sleep(1500);
		                while (!lbowu.validate()) {
		                        Task.sleep(100);
		                        if (!timer.isRunning())
		                                break;
		                }
		                if(lbowu.validate()) {
		                        Mouse.click(lbowu.getCentralPoint(), true);
		                }
		                while (!fletch.validate()) {
		                        Task.sleep(100);
		                        if (!timer.isRunning())
		                                break;
		                }
		                if(fletch.validate()) {
		                        Mouse.click(fletch.getCentralPoint(), true);
		                }
		                Task.sleep(50000);
		                bank.interact("Bank");
		                Task.sleep(1250);
					}
				}

				@Override
		        public void messageReceived(MessageEvent e) {
		   					if(e.getMessage().equals("You carefully cut the wood into a Maple shieldbow (u)."))
		                                fletchesdone++;                
		   		}
				 
			 }
			 
			 public class yfletchsbu extends Node implements MessageListener {

					@Override
					public boolean activate() {
						return Inventory.getCount(logs) > 0;
					}

					@Override
					public void execute() {
						NPC bank = NPCs.getNearest(banker);
						Item log = Inventory.getItem(logs);
						WidgetChild fletch = Widgets.get(1371, 5);
						WidgetChild sbowu = Widgets.get(1371, 44).getChild(0);
						final Timer timer = new Timer(5000);
						if (log.getWidgetChild().interact("Craft")) {
							Task.sleep(1500);
			                while (!sbowu.validate()) {
			                        Task.sleep(100);
			                        if (!timer.isRunning())
			                                break;
			                }
			                if(sbowu.validate()) {
			                        Mouse.click(sbowu.getCentralPoint(), true);
			                }
			                while (!fletch.validate()) {
			                        Task.sleep(100);
			                        if (!timer.isRunning())
			                                break;
			                }
			                if(fletch.validate()) {
			                        Mouse.click(fletch.getCentralPoint(), true);
			                }
			                Task.sleep(51000);
			                bank.interact("Bank");
			                Task.sleep(1250);
						}
					}
					@Override
			        public void messageReceived(MessageEvent e) {
			   					if(e.getMessage().equals("You carefully cut the wood into a Yew shortbow (u)."))
			                                fletchesdone++;                
			   		}
					 
				 }
				 
				 public class yfletchlbu extends Node  implements MessageListener{

					@Override
					public boolean activate() {
						return Inventory.getCount(logs) > 0;
					}

					@Override
					public void execute() {
						NPC bank = NPCs.getNearest(banker);
						Item log = Inventory.getItem(logs);
						WidgetChild fletch = Widgets.get(1371, 5);
						WidgetChild lbowu = Widgets.get(1371, 44).getChild(8);
						final Timer timer = new Timer(5000);
						if (log.getWidgetChild().interact("Craft")) {
							Task.sleep(1500);
			                while (!lbowu.validate()) {
			                        Task.sleep(100);
			                        if (!timer.isRunning())
			                                break;
			                }
			                if(lbowu.validate()) {
			                        Mouse.click(lbowu.getCentralPoint(), true);
			                }
			                while (!fletch.validate()) {
			                        Task.sleep(100);
			                        if (!timer.isRunning())
			                                break;
			                }
			                if(fletch.validate()) {
			                        Mouse.click(fletch.getCentralPoint(), true);
			                }
			                Task.sleep(50000);
			                bank.interact("Bank");
			                Task.sleep(1250);
						}
					}

					@Override
			        public void messageReceived(MessageEvent e) {
			   					if(e.getMessage().equals("You carefully cut the wood into a Yew shieldbow (u)."))
			                                fletchesdone++;                
			   		}
					 
				 }
				 
				 public class fletchmsbu extends Node implements MessageListener {

						@Override
						public boolean activate() {
							return Inventory.getCount(logs) > 0;
						}

						@Override
						public void execute() {
							NPC bank = NPCs.getNearest(banker);
							Item log = Inventory.getItem(logs);
							WidgetChild fletch = Widgets.get(1371, 5);
							WidgetChild sbowu = Widgets.get(1371, 44).getChild(0);
							final Timer timer = new Timer(5000);
							if (log.getWidgetChild().interact("Craft")) {
								Task.sleep(1500);
				                while (!sbowu.validate()) {
				                        Task.sleep(100);
				                        if (!timer.isRunning())
				                                break;
				                }
				                if(sbowu.validate()) {
				                        Mouse.click(sbowu.getCentralPoint(), true);
				                }
				                while (!fletch.validate()) {
				                        Task.sleep(100);
				                        if (!timer.isRunning())
				                                break;
				                }
				                if(fletch.validate()) {
				                        Mouse.click(fletch.getCentralPoint(), true);
				                }
				                Task.sleep(51000);
				                bank.interact("Bank");
				                Task.sleep(1250);
							}
						}
						@Override
				        public void messageReceived(MessageEvent e) {
				   					if(e.getMessage().equals("You carefully cut the wood into a Magic shortbow (u)."))
				                                fletchesdone++;                
				   		}
						 
					 }
	 
	 public class fletchlmbu extends Node implements MessageListener{

		@Override
		public boolean activate() {
			return Inventory.getCount(logs) > 0;
		} 

		@Override
		public void execute() {
			NPC bank = NPCs.getNearest(banker);
			Item log = Inventory.getItem(logs);
			WidgetChild fletch = Widgets.get(1371, 5);
			WidgetChild lbowu = Widgets.get(1371, 44).getChild(4);
			final Timer timer = new Timer(5000);
			if (log.getWidgetChild().interact("Craft")) {
				Task.sleep(1500);
                while (!lbowu.validate()) {
                        Task.sleep(100);
                        if (!timer.isRunning())
                                break;
                }
                if(lbowu.validate()) {
                        Mouse.click(lbowu.getCentralPoint(), true);
                }
                while (!fletch.validate()) {
                        Task.sleep(100);
                        if (!timer.isRunning())
                                break;
                }
                if(fletch.validate()) {
                        Mouse.click(fletch.getCentralPoint(), true);
                }
                Task.sleep(51000);
                bank.interact("Bank");
                Task.sleep(1250);
			}
		}

		@Override
        public void messageReceived(MessageEvent e) {
   					if(e.getMessage().equals("You carefully cut the wood into a Magic shieldbow (u)."))
                                fletchesdone++;                
   		}
		 
	 }
	 
	 public class GUI extends JFrame {

     	/**
     	 * 
     	 */
     	private static final long serialVersionUID = 1L;

     	GridLayout grid;
     	Container pane;

     	JLabel label1 = new JLabel("AIO Fletcher by ArcaneTunar");
     	JComboBox<String> comboType = new JComboBox<String>();
     	JButton btnStart = new JButton("Start");

     	GUI() {

     		pane = getContentPane();
     		grid = new GridLayout(3, 0);

     		grid.setVgap(20);

     		this.setLayout(grid);
     		this.setSize(250, 150);
     		this.setResizable(false);
     		this.setAlwaysOnTop(true);
     		this.setVisible(true);
     		this.setLocationRelativeTo(null);

     		label1.setHorizontalAlignment(JLabel.CENTER);
     		pane.add(label1);

     		comboType.setModel(new DefaultComboBoxModel<>(new String[] {
     				"Shortbow", "Shieldbow", "Oak shortbow", "Oak shieldbow",
     				"Willow shortbow", "Willow shieldbow", "Maple shortbow",
     				"Maple shieldbow", "Yew shortbow", "Yew shieldbow",
     				"Magic shortbow", "Magic shieldbow" }));
     		pane.add(comboType);

     		btnStart.addActionListener(new ActionListener() {

     			@Override
     			public void actionPerformed(ActionEvent e) {
     				startScript();

     			}

     		});
     		pane.add(btnStart);

     	}

     	public void startScript() {
     		switch(comboType.getSelectedIndex()) {
     		case 0 :
     			//shortbow
     			logs = 1511;
     			jobContainer = new Tree(new Node[] { new sbowfletcher(), new banker()});
     			break;
     		case 1:
     			//shieldbow
     			logs = 1511;
     			jobContainer = new Tree(new Node[] {new lbowfletcher(), new banker()});
     			break;
     		case 2: 
     			//oak shortbow
     			logs = 1521;
     			jobContainer = new Tree(new Node[] {new fletchsbu(), new banker()});
     			break;
     		case 3: 
     			//oak shieldbow
     			logs = 1521;
     			jobContainer = new Tree(new Node[] {new fletchlbu(), new banker()});
     			break;
     		case 4:
     			//willow shortbow
     			logs = 1519;
     			jobContainer = new Tree(new Node[] {new wfletchsbu(), new banker()});
     			break;
     		case 5:
     			//willow shieldbow
     			logs = 1519;
     			jobContainer = new Tree(new Node[] {new wfletchlbu(), new banker()});
     			break;
     		case 6:
     			//maple shortbow
     			logs = 1517;
     			jobContainer = new Tree(new Node[] {new mfletchsbu(), new banker()});
     			break;
     		case 7: 
     			//maple shieldbow
     			logs = 1517;
     			jobContainer = new Tree(new Node[] {new mfletchlbu(), new banker()});
     			break;
     		case 8:
     			//yew shortbow
     			logs = 1515;
     			jobContainer = new Tree(new Node[] {new yfletchsbu(), new banker()});
     			break;
     		case 9:
     			//yew shieldbow
     			logs = 1515;
     			jobContainer = new Tree(new Node[] {new yfletchlbu(), new banker()});
     			break;
     		case 10: 
     			//magic shortbow
     			logs = 1513;
     			jobContainer = new Tree(new Node[] {new fletchmsbu(), new banker()});
     			break;
     		case 11:
     			//magic shieldbow
     			logs = 1513;
     			jobContainer = new Tree(new Node[]{new fletchlmbu(), new banker()});
     			break;
     		}
     		this.dispose();
     	}
     }


	    public void onRepaint(Graphics g1) {
	        Graphics2D g = (Graphics2D)g1;
	        int fleg = Skills.getExperience(Skills.FLETCHING) - startexp;
	        g.setColor(color1);
	        g.fillRect(545, 254, 194, 261);
	        g.setColor(color2);
	        g.setStroke(stroke1);
	        g.drawRect(545, 254, 194, 261);
	        g.setFont(font1);
	        g.setColor(color3);
	        g.drawString("ArcaneTunar", 563, 283 );
	        g.setColor(color4);
	        g.drawString("AIOFletcher", 587, 303 );
	        g.setFont(font2);
	        g.setColor(color5);
	        g.drawString("BETA", 668, 324);
	        g.setColor(color6);
	        g.drawString("Time: " +timer.toElapsedString(), 560, 350);
	        g.drawString("Fletched: "+fletchesdone, 560, 370);
	        g.drawString("Exp Gained: "+fleg, 560, 390);
	        drawMouse(g1);
	    }
	 public void drawMouse(Graphics g) {
         g.setColor(Color.RED);
         int mouseY = (int) Mouse.getLocation().getY();
         int mouseX = (int) Mouse.getLocation().getX();
         g.drawLine(mouseX - 5, mouseY + 5, mouseX + 5, mouseY - 5);
         }
}

