import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.border.EmptyBorder;

import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

@Manifest(authors = { "arcanetunar" }, name = "Arcanetunar_Bonfire", description = "Makes bonfires for FM exp All logs supported!!!")
public class Arcanetunar_Bonfire extends ActiveScript implements PaintListener {

  guiTest gui;
	public int Log_Id;
	public long Starttime;
	public String Status;
	public SceneObject fire;
	public int startlvl;
	public int fsc;
	public NPC FireSpirit;
	public int startexp;
	public int startscript = 0;
	public int[] bonfire_ids = { 70755, 70757, 70761, 70764, 70758, 70765 };
	public final Tile bankTile = new Tile(3151, 3477, 0);
	public final Tile BurnTile = new Tile(3141, 3483, 0);

	public void onRepaint(Graphics g1) {
		int fmlg = Skills.getLevel(Skills.FIREMAKING) - startlvl;
		int fmeg = Skills.getExperience(Skills.FIREMAKING) - startexp;
		long Runtime = System.currentTimeMillis() - Starttime;
		Graphics2D g = (Graphics2D) g1;
		g.translate(0, 50);
		g.drawImage(img1, 6, 343, null);
		g.setFont(font1);
		g.setColor(color1);
		g.drawString("Arcanetunar_Bonfire", 141, 370);
		g.setFont(font2);
		g.drawString("Firemaking level: " + Skills.getLevel(Skills.FIREMAKING)
				+ "(+" + fmlg + ")", 87, 396);
		g.drawString("Firemaking Exp Gained: " + fmeg, 86, 416);
		g.drawString("Status: " + Status, 145, 467);
		g.drawString("RunTime: " + Time.format(Runtime), 144, 451);
		g.drawString("Fire Spirits Collected: " + fsc, 86, 435);
		g.drawString("Version: " + "1", 399, 357);
	}

	public class Checker extends Node {

		@Override
		public boolean activate() {
			return true;
		}

		@Override
		public void execute() {
			if (!Tabs.INVENTORY.isOpen()) {
				Tabs.INVENTORY.open();
			}
			while (Camera.getPitch() <= 50) {
				Camera.setPitch(Random.nextInt(75, 91));
			}
			if (Walking.getEnergy() <= 20) {
				Widgets.get(750, 0).interact("Rest");
			}
			if (!Walking.isRunEnabled()) {
				Walking.setRun(true);
			}
		}

	}

	@Override
	public void onStart() {
		startlvl = Skills.getLevel(Skills.FIREMAKING);
		gui = new guiTest();
		gui.setVisible(true);
		startexp = Skills.getExperience(Skills.FIREMAKING);
		Starttime = System.currentTimeMillis();
	}

	public class Firespirits extends Node {

		@Override
		public boolean activate() {
			return FireSpirit != null && FireSpirit.isOnScreen();
		}

		@Override
		public void execute() {
			if (Bank.isOpen()) {
				Bank.close();
			}
			if (Walking.walk(FireSpirit.getLocation())) {
				Status = "Fire Spirit Found, Walking To It";
				if (FireSpirit.hover()) {
					Status = "Collecting Fire Spirit";
					if (FireSpirit.interact("Collect-reward")) {
						fsc++;
						Task.sleep(4000, 5000);
						Status = "Fire Spirit Collected";
					}
				}
			}
		}

	}

	@SuppressWarnings("serial")
	private class guiTest extends JFrame {
		private void button1MouseClicked(MouseEvent e) {
			if (comboBox1.getSelectedItem().equals("Normal Logs")) {
				Log_Id = 1511;
				gui.setVisible(false);
				startscript = 1;
			} else if (comboBox1.getSelectedItem().equals("Oak Logs")) {
				Log_Id = 1521;
				gui.setVisible(false);
				startscript = 1;
			} else if (comboBox1.getSelectedItem().equals("Willow Logs")) {
				Log_Id = 1519;
				gui.setVisible(false);
				startscript = 1;
			} else if (comboBox1.getSelectedItem().equals("Maple Logs")) {
				Log_Id = 1517;
				gui.setVisible(false);
				startscript = 1;
			} else if ((comboBox1.getSelectedItem().equals("Yew Logs"))) {
				Log_Id = 1515;
				gui.setVisible(false);
				startscript = 1;
			} else if ((comboBox1.getSelectedItem().equals("Magic Logs"))) {
				Log_Id = 1513;
				gui.setVisible(false);
				startscript = 1;
			}

		}

		public guiTest() {

			dialogPane = new JPanel();
			contentPanel = new JPanel();
			label1 = new JLabel();
			comboBox1 = new JComboBox<>();
			buttonBar = new JPanel();
			button1 = new JButton();

			setTitle("Arcanetunar_Bonfire");
			Container contentPane = getContentPane();
			contentPane.setLayout(new BorderLayout());

			{
				dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));

				dialogPane.setBorder(new javax.swing.border.CompoundBorder(
						new javax.swing.border.TitledBorder(
								new javax.swing.border.EmptyBorder(0, 0, 0, 0),
								"", javax.swing.border.TitledBorder.CENTER,
								javax.swing.border.TitledBorder.BOTTOM,
								new java.awt.Font("Dialog", java.awt.Font.BOLD,
										12), java.awt.Color.red), dialogPane
								.getBorder()));
				dialogPane
						.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
							public void propertyChange(
									java.beans.PropertyChangeEvent e) {
								if ("border".equals(e.getPropertyName()))
									throw new RuntimeException();
							}
						});

				dialogPane.setLayout(new BorderLayout());

				{

					label1.setText("Log To Burn:");

					comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
							"Normal Logs", "Oak Logs ", "Willow Logs",
							"Maple Logs", "Yew Logs", "Magic Logs" }));

					GroupLayout contentPanelLayout = new GroupLayout(
							contentPanel);
					contentPanel.setLayout(contentPanelLayout);
					contentPanelLayout
							.setHorizontalGroup(contentPanelLayout
									.createParallelGroup()
									.addGroup(
											contentPanelLayout
													.createSequentialGroup()
													.addComponent(label1)
													.addPreferredGap(
															LayoutStyle.ComponentPlacement.RELATED)
													.addComponent(
															comboBox1,
															GroupLayout.PREFERRED_SIZE,
															141,
															GroupLayout.PREFERRED_SIZE)
													.addGap(0, 54,
															Short.MAX_VALUE)));
					contentPanelLayout
							.setVerticalGroup(contentPanelLayout
									.createParallelGroup()
									.addGroup(
											contentPanelLayout
													.createSequentialGroup()
													.addGroup(
															contentPanelLayout
																	.createParallelGroup(
																			GroupLayout.Alignment.BASELINE)
																	.addComponent(
																			label1)
																	.addComponent(
																			comboBox1,
																			GroupLayout.PREFERRED_SIZE,
																			GroupLayout.DEFAULT_SIZE,
																			GroupLayout.PREFERRED_SIZE))
													.addContainerGap(
															GroupLayout.DEFAULT_SIZE,
															Short.MAX_VALUE)));
				}
				dialogPane.add(contentPanel, BorderLayout.CENTER);

				{
					buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
					buttonBar.setLayout(new GridBagLayout());
					((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[] {
							0, 85, 80 };
					((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[] {
							1.0, 0.0, 0.0 };

					button1.setText("~~Start Script~~");
					button1.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							button1MouseClicked(e);
						}
					});
					buttonBar.add(button1, new GridBagConstraints(0, 0, 3, 1,
							0.0, 0.0, GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
							0));
				}
				dialogPane.add(buttonBar, BorderLayout.SOUTH);
			}
			contentPane.add(dialogPane, BorderLayout.CENTER);
			setSize(300, 120);
			setLocationRelativeTo(getOwner());

		}

		private JPanel dialogPane;
		private JPanel contentPanel;
		private JLabel label1;
		private JComboBox<String> comboBox1;
		private JPanel buttonBar;
		private JButton button1;

	}

	public class firelogs extends Node {

		@Override
		public boolean activate() {
			return Tabs.INVENTORY.isOpen() && Inventory.getCount(Log_Id) > 0;
		}

		@Override
		public void execute() {
			if (Bank.isOpen()) {
				Bank.close();
			}
			FireSpirit = NPCs.getNearest(15451);
			fire = SceneEntities.getNearest(bonfire_ids);
			Item log = Inventory.getItem(Log_Id);
			if (fire == null) {
				if (log != null) {
					if (log.getWidgetChild().validate()) {
						if (BurnTile != null) {
							if (!BurnTile.isOnScreen()) {
								Camera.turnTo(BurnTile);
								Walking.walk(BurnTile);
							} else if (BurnTile.isOnScreen()) {
								if (Walking.walk(BurnTile)) {
									if (log.getWidgetChild().hover()) {
										if (log.getWidgetChild().interact(
												"Light")) {
											Task.sleep(800, 1000);
										}
									}// rsbot froze because he widget explorer
										// is fuckign retarded.
										// do you know how to get widgets? not
										// really :§
										// always run it with the cmd so yuo can
										// see any errors ok
								}
							}
						} else if (BurnTile == null) {
							if (log.getWidgetChild().hover()) {
								if (log.getWidgetChild().interact("Light")) {
									Task.sleep(800, 1000);
								}
								if (log.getWidgetChild().hover()) {
									if (log.getWidgetChild().interact("Light")) {
										Task.sleep(800, 1000);
									}
								}
							}
						}
					}
				}
			} else if (fire != null) {

				if (!fire.isOnScreen()) {
					if (Calculations.distanceTo(fire.getLocation()) <= 75) {
						Camera.turnTo(fire);
						Walking.walk(fire.getLocation());
					}
				} else if (fire.isOnScreen()) { // 1179, 33 is the widgetchild
					if (fire.interact("Use")) {
						final WidgetChild screen = Widgets.get(1179, 33);
						final Timer timer = new Timer(5000);
						while (!screen.validate()) {
							Task.sleep(100);
							if (!timer.isRunning())
								break;
						}
						if(screen.validate()) {
							Mouse.click(screen.getCentralPoint(), true);
						}
						Status = "Burning Logs";
						Task.sleep(10000, 15000);

					}

				}
			}

		}

	}

	public class AntiBan extends Node {

		@Override
		public boolean activate() {
			return Game.isLoggedIn();
		}

		@Override
		public void execute() {
			if (Random.nextInt(1, 10000) > 100) {
				Camera.setPitch(Random.nextInt(70, 91));
				Task.sleep(Random.nextInt(1000, 5000));
				Tabs.EQUIPMENT.open();
				Mouse.move(Random.nextInt(1, 500), Random.nextInt(1, 500));

			} else {
				Task.sleep(Random.nextInt(1000, 5000));
				Camera.setPitch(Random.nextInt(70, 91));
				Tabs.INVENTORY.open();
				Mouse.move(Random.nextInt(1, 500), Random.nextInt(1, 500));

			}

		}

	}

	public class getlogs extends Node {

		@Override
		public boolean activate() {
			return Tabs.INVENTORY.isOpen() && Inventory.getCount(Log_Id) == 0;
		}

		@Override
		public void execute() {
			if (Bank.isOpen()) {
				Status = "Withdrawing More Logs...";
				if (Bank.withdraw(Log_Id, 28)) {
					Task.sleep(900, 1400);
					Status = "Withdrawed";
					Task.sleep(300, 500);
					Status = "Closing Bank";
					if (Bank.close()) {
						Task.sleep(800, 1000);
						Status = "Bank Closed, Going To Burn More Logs";
					}
				}
			} else if (!Bank.isOpen()) {
				if (Bank.getNearest() != null) {
					if (!Bank.getNearest().isOnScreen()) {
						if (bankTile != null) {
							Camera.turnTo(bankTile);
							Walking.walk(bankTile);
						}
					} else if (Bank.getNearest().isOnScreen()) {
						if (Bank.open()) {
							Task.sleep(800, 1000);
						}
					}
				}

			}

		}
	}

	private Image getImage(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			return null;
		}
	}

	private final Color color1 = new Color(0, 0, 0);

	private final Font font1 = new Font("Adobe Arabic", 0, 22);
	private final Font font2 = new Font("Adobe Arabic", 0, 13);

	private final Image img1 = getImage("http://s12.postimage.org/c1up14rnh/ssdds.png");

	private Tree jobContainer = null;

	@Override
	public int loop() {
		if (jobContainer != null) {
			final Node job = jobContainer.state();
			if (job != null) {
				jobContainer.set(job);
				getContainer().submit(job);
				job.join();
			}
		} else {
			jobContainer = new Tree(new Node[] { new getlogs(), new firelogs(),
					new AntiBan(), new Firespirits(), new Checker() });
		}
		return Random.nextInt(100, 200);
	}

}
