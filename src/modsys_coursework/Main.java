/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modsys_coursework;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Vector;


/**
 *
 * @author dshmelyo
 */
public class Main extends Applet implements ActionListener, Runnable {
        Thread thread = new Thread(this);
	Vector TimeOfComin;
	Vector TimeOfResolv;
	Vector TimeOfRecord;
	int People = 300;
	int var_Speed = 1;
	Button slowButton, runButton, fastButton;
	
	//Variable 0f Statistics
	int CountMinutes = 0;
	int CountWaitMinutes = 0;
	int CountWaiters = 0;
	Vector ListOfWaiters;
	Vector ListOfCountWaiters;

	@Override
	public void init() {
		TimeOfComin = new Vector();
		TimeOfResolv = new Vector();
		TimeOfRecord = new Vector();
		ListOfWaiters = new Vector();
		ListOfCountWaiters = new Vector();

		slowButton = new Button("<<");
		runButton = new Button("Action!");
		fastButton = new Button(">>");
		add(slowButton);
		add(runButton);
		add(fastButton);
		slowButton.addActionListener(this);
		runButton.addActionListener(this);
		fastButton.addActionListener(this);

		setBackground(Color.black);
		Random randomGenerator = new Random();
		//Nulling ListOfWaiters
		for (int idx = 0; idx <= People; ++idx) {
			ListOfWaiters.addElement(0);
		}
		//Incoming
		for (int idx = 0; idx <= People; ++idx) {
			// 15 +- 5 min for come in to class
			int income_time = 10 + randomGenerator.nextInt(10);
			TimeOfComin.addElement(income_time);
		}
		//Resolving
		for (int idx = 0; idx <= People; ++idx) {
			// 15 +- 10 min for resolve labwork
			int resolve_time = 5 + randomGenerator.nextInt(20);
			TimeOfResolv.addElement(resolve_time);
		}
		//Writing: select people
		for (int idx = 0; idx <= People; ++idx) {
			// 3 +- 2 min for record info
			int select = randomGenerator.nextInt(3);
			int record_time = 0;
			if (select == 2) {
				record_time = 1 + randomGenerator.nextInt(4);
			}
			TimeOfRecord.addElement(record_time);
		}
	}

	@Override
	public void paint(Graphics g) {
		double width = (double) getSize().width / People;
		for (int i = 0; i < People; i++) {
			int x1 = (int) (i * width + 1);
			int x2 = (int) (x1 + width - 1);

			int y = (Integer) TimeOfComin.elementAt(i) * 10;
			g.setColor(Color.RED);
			g.fillRect(x1, getSize().height - y, (int) width, y);

			int old_y = y;
			y = y + (Integer) TimeOfResolv.elementAt(i) * 10;
			g.setColor(Color.BLUE);
			g.fillRect(x1, getSize().height - y, (int) width, y - old_y);

			old_y = y;
			y = y + (Integer) TimeOfRecord.elementAt(i) * 10;
			g.setColor(Color.WHITE);
			g.fillRect(x1, getSize().height - y, (int) width, y - old_y);
		}

		g.setColor(Color.DARK_GRAY);
		for (int i = 0; i < 60; i++) {
			g.drawLine(0, getSize().height - 10 * i, getSize().width, getSize().height - 10 * i);
		}
		g.setColor(Color.WHITE);
		g.drawString(CountMinutes + " Прошло минут", 1, 15);
		
		CountWaiters=0;
		for (int i = 0; i < People; i++) {
			if ((Integer) ListOfWaiters.elementAt(i) > 0) CountWaiters++;
		}
		g.drawString(CountWaiters + " Число пользователей в очереди", 1, 30);

		double count = 0;
		for (int i=0; i< ListOfCountWaiters.size();i++) {
			count = count + (Integer) ListOfCountWaiters.elementAt(i);
		}
		if (count != 0) {
			count = count / ListOfCountWaiters.size();
		}
		g.drawString(count + " Средняя длина очереди", 1, 45);
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == runButton) {
				thread.start();
		}
		if (event.getSource() == slowButton) {
				var_Speed = var_Speed / 2;
				if (var_Speed == 0) var_Speed = 1;
		}
		if (event.getSource() == fastButton) {
				var_Speed = var_Speed * 2;
		}
	}

	public void RunThread() {
		int var_TimeOfComin = (Integer) TimeOfComin.elementAt(People - 1);
		int var_TimeOfResolv = (Integer) TimeOfResolv.elementAt(People - 1);
		int var_TimeOfRecord = (Integer) TimeOfRecord.elementAt(People - 1);


		for (int i = 0; i < People; i++) {
			var_TimeOfComin = (Integer) TimeOfComin.elementAt(i);
			var_TimeOfResolv = (Integer) TimeOfResolv.elementAt(i);
			var_TimeOfRecord = (Integer) TimeOfRecord.elementAt(i);
			if (var_TimeOfComin == 0 && var_TimeOfResolv != 0) {
				TimeOfResolv.set(i, var_TimeOfResolv - 1);
				int PeopleCount = 0;
				for (int j = i+7; j < People; j++) {
					if ((Integer) TimeOfComin.elementAt(j) == 0) {
						int var_waiter = (Integer) ListOfWaiters.elementAt(j);
						CountWaitMinutes++;
						PeopleCount++;
						ListOfWaiters.set(j,var_waiter + 1);
					} else {
						break;
					}
				}
				if (PeopleCount != 0) ListOfCountWaiters.add(PeopleCount);
				break;
			}
			if (var_TimeOfComin == 0 && var_TimeOfResolv == 0 && var_TimeOfRecord != 0) {
				TimeOfRecord.set(i, var_TimeOfRecord - 1);
				break;
			}
		}

		for (int i = 0; i < People; i++) {
			var_TimeOfComin = (Integer) TimeOfComin.elementAt(i);
			if (var_TimeOfComin != 0) {
				TimeOfComin.set(i, var_TimeOfComin - 1);
				break;
			}
		}
	}

	public void run() {
		while (true) {
			CountMinutes++;
			int var_TimeOfComin = (Integer) TimeOfComin.elementAt(People - 1);
			int var_TimeOfResolv = (Integer) TimeOfResolv.elementAt(People - 1);
			int var_TimeOfRecord = (Integer) TimeOfRecord.elementAt(People - 1);
			if (var_TimeOfComin + var_TimeOfResolv + var_TimeOfRecord == 0) {
				break;
			}
			RunThread();
			repaint();
			try {
				Thread.sleep(1000/var_Speed);
			} catch (InterruptedException ex) {
			}
		}
	}

	
}
