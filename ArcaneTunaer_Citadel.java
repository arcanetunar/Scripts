import java.awt.Container;
import java.awt.EventQueue;
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

import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.node.SceneObject;


@Manifest(authors = { "arcanetunar" }, description = "Completes Citadel for you!", name = "ArcaneTunar Citadel")
public class Citadel  extends ActiveScript{
  
	private final List<Node> jobsCollection = Collections.synchronizedList(new ArrayList<Node>());
	private Tree jobContainer = null;
	
	public Area t;
	
	public int tree = 18859;
	public int stone = 21738;
	public int ore = 25052;
	public int pore = 25062;
	public int oref = 27272;
	public int mouldf = 27161;
	public int coolerf = 27113;
	public int poref = 27317;
	public int kiln = 25242;
	public int grill = 29838;
	public int loom = 14435;
	public int obelisk = 20585;
	
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
	
	public class timber extends Node {
		SceneObject ttree = SceneEntities.getNearest(tree);
		@Override
		public boolean activate() {
			return ttree.isOnScreen();
		}

		@Override
		public void execute() {
			ttree.interact("Chop");
			Task.sleep(5000, 7000);
		}
	}
	
	public class mstone extends Node {
		SceneObject mine = SceneEntities.getNearest(stone);
		@Override
		public boolean activate() {
			return mine.isOnScreen();
		}

		@Override
		public void execute() {
			mine.interact("Mine");
			Task.sleep(5000, 7000);
		}
	}
	
	public class more extends Node {
		SceneObject mine = SceneEntities.getNearest(ore);
		@Override
		public boolean activate() {
			return mine.isOnScreen();
		}

		@Override
		public void execute() {
			mine.interact("Mine");
			Task.sleep(5000, 7000);
		}
	}
	
	public class mpore extends Node {
		SceneObject mine = SceneEntities.getNearest(pore);
		@Override
		public boolean activate() {
			return mine.isOnScreen();
		}

		@Override
		public void execute() {
			mine.interact("Mine");
			Task.sleep(5000, 7000);
		}
	}
	
	public class summ extends Node {
		SceneObject sum = SceneEntities.getNearest(obelisk);
		@Override
		public boolean activate() {
			return sum.isOnScreen();
		}

		@Override
		public void execute() {
			sum.interact("Summon");
			Task.sleep(2500, 5000);
			
		}
	}
	
	 public class GUI extends JFrame {

	     	/**
	     	 * 
	     	 */
	     	private static final long serialVersionUID = 1L;

	     	GridLayout grid;
	     	Container pane;

	     	JLabel label1 = new JLabel("Citadel by ArcaneTunar");
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
	     				"Timber", "Stone", "Ore", "Precious Ore", "Obelisk" }));
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
	     			jobContainer = new Tree(new Node[]{new timber()});
	     			break;
	     		case 1:
	     			jobContainer = new Tree(new Node[]{new mstone()});
	     			break;
	     		case 2: 
	     			jobContainer = new Tree(new Node[]{new more()});
	     			break;
	     		case 3: 
	     			jobContainer = new Tree(new Node[]{new mpore()});
	     			break;
	     		case 4:
	     			jobContainer = new Tree(new Node[]{new summ()});
	     			break;
	     		}
	     		this.dispose();
	     	}
	     }

}
