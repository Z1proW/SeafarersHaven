import sound.Sound;
import view.Texture;

import javax.swing.*;
import javax.swing.plaf.metal.MetalSliderUI;
import java.awt.*;

public class Menu extends JFrame
{

	public Menu()
	{
		setTitle(Game.TITLE);
		setIconImage(Game.ICON);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width / 2, screenSize.height / 2);
		setPreferredSize(getSize());

		JPanel backgroundPanel = new JPanel()
		{
			private static final ImageIcon BACKGROUND = new ImageIcon(Texture.getImage("menu.jpg"));

			@Override
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				g.drawImage(BACKGROUND.getImage(), 0, 0, getWidth(), getHeight(), null);
			}
		};

		JPanel buttonPanel = new JPanel(new GridBagLayout());
		buttonPanel.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		JButton play = new JButton("Jouer");
		play.setFocusable(false);

		JSlider mapSize = new JSlider(JSlider.HORIZONTAL, 50, 500, 100);
		mapSize.setToolTipText("Taille de la carte");
		mapSize.setFocusable(false);
		mapSize.setSnapToTicks(true);
		mapSize.setMajorTickSpacing(50);
		mapSize.setUI(new MetalSliderUI()
		{
			@Override
			protected void scrollDueToClickInTrack(int direction)
			{
				int value = slider.getValue();

				if (slider.getOrientation() == JSlider.HORIZONTAL)
					value = this.valueForXPosition(slider.getMousePosition().x);

				else if (slider.getOrientation() == JSlider.VERTICAL)
					value = this.valueForYPosition(slider.getMousePosition().y);

				slider.setValue(value);
			}
		});
		mapSize.setLabelTable(mapSize.createStandardLabels(100, 100));
		mapSize.setToolTipText("Taille de la carte");
		mapSize.setPaintLabels(true);

		JButton quit = new JButton("Quitter");
		quit.setFocusable(false);

		play.addActionListener(e ->
		{
			Sound.CLICK.play();
			dispose();
			Game.start(mapSize.getValue());
		});

		quit.addActionListener(e ->
		{
			Sound.CLICK.play();
			System.exit(0);
		});

		buttonPanel.add(play, gbc);
		gbc.gridy++;
		buttonPanel.add(mapSize, gbc);
		gbc.gridy++;
		buttonPanel.add(quit, gbc);

		backgroundPanel.setLayout(new BorderLayout());
		backgroundPanel.add(buttonPanel, BorderLayout.CENTER);

		setContentPane(backgroundPanel);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
