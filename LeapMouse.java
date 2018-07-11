package guicds;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Gesture.State;
import com.leapmotion.leap.Gesture.Type;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.InteractionBox;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;


class CustomListener extends Listener {
	public Robot robot;
	public boolean z = true;
	
	 public boolean keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {

       case KeyEvent.VK_LEFT:
           z = true;
           break;

       case KeyEvent.VK_RIGHT:
       		z = false;
           break;

       case KeyEvent.VK_UP:
       		z = false;
           break;
           
       case KeyEvent.VK_DOWN:
       		z = false;
           break;
		}
        return z;
	}	
	
	
	public void onConnect(Controller c){
		try { robot = new Robot(); } catch (Exception e) {}
		c.enableGesture(Gesture.Type.TYPE_CIRCLE);
		c.enableGesture(Gesture.Type.TYPE_SWIPE);
		c.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
		c.enableGesture(Gesture.Type.TYPE_KEY_TAP);
		c.setPolicy(Controller.PolicyFlag.POLICY_BACKGROUND_FRAMES);
		c.setPolicy(Controller.PolicyFlag.POLICY_IMAGES);
	}
	
	public void onFrame(Controller c) {
		//while(z=false){
		Frame frame = c.frame();
		HandList hands = frame.hands();
		Hand furthestRight = frame.hands().rightmost();
		Hand furthestLeft = frame.hands().leftmost();
		
		InteractionBox box = frame.interactionBox();
		
		for (Finger f : frame.fingers()) {
			Hand attachedHand = f.hand();
			if (attachedHand.isRight()){
			if (f.type() == Finger.Type.TYPE_INDEX) {
				Vector fingerPos = f.stabilizedTipPosition();
				Vector boxFingerPosition = box.normalizePoint(fingerPos);
				Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
				if (attachedHand.isRight()){
				robot.mouseMove((int) (screen.width * boxFingerPosition.getX()), (int) (screen.height - boxFingerPosition.getY() * screen.height));
				}
				}
			}
			
		}
		
		
		
		Hand hi = null;
		for (Gesture g : frame.gestures()) {
			hands = g.hands();
			for (int index = 0; index < hands.count(); index++) {
			    hi = hands.get(index);
			}
			if (furthestRight.id()==hi.id()){
			if (g.type() == Type.TYPE_CIRCLE) {
				CircleGesture circle = new CircleGesture(g);
				if(circle.pointable().direction().angleTo(circle.normal()) <= (Math.PI/4)){
					//robot.mousePress(InputEvent.BUTTON1_MASK);
					//robot.mouseRelease(InputEvent.BUTTON1_MASK);
					try { Thread.sleep(50); } catch (Exception e) {}
				} else {
					//robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
					//robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
					try { Thread.sleep(50); } catch (Exception e) {}
				}
			} else if (g.type() == Type.TYPE_SCREEN_TAP){
				//robot.mousePress(InputEvent.BUTTON1_MASK);
				//robot.mouseRelease(InputEvent.BUTTON1_MASK);
				//try { Thread.sleep(50); } catch (Exception e) {}
			} else if (g.type() == Type.TYPE_SWIPE && g.state()==State.STATE_START) {
				//robot.mouseWheel(1);
				//try { Thread.sleep(50); } catch (Exception e) {}
			} else if (g.type() == Type.TYPE_KEY_TAP) {
				//robot.keyPress(KeyEvent.VK_WINDOWS);
				//robot.keyRelease(KeyEvent.VK_WINDOWS);
				//try { Thread.sleep(50); } catch (Exception e) {}
			}
			}
			if (furthestLeft.id()==hi.id()){
				if (g.type() == Type.TYPE_CIRCLE) {
					CircleGesture circle = new CircleGesture(g);
					if(circle.pointable().direction().angleTo(circle.normal()) <= (Math.PI/4)){
						//robot.keyPress(KeyEvent.VK_Q);
						//robot.keyRelease(KeyEvent.VK_Q);
						try {
							java.awt.Desktop.getDesktop().browse(java.net.URI.create("http://google.com"));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try { Thread.sleep(500); } catch (Exception e) {}
					} else {
						//robot.ekeyPress(KeyEvent.VK_W);
						//robot.keyRelease(KeyEvent.VK_W);
						try { Thread.sleep(50); } catch (Exception e) {}
						}
					} else if (g.type() == Type.TYPE_SCREEN_TAP){
						robot.keyPress(KeyEvent.VK_E);
						robot.keyRelease(KeyEvent.VK_E);
						try { Thread.sleep(50); } catch (Exception e) {}
					} else if (g.type() == Type.TYPE_SWIPE && g.state()==State.STATE_START) {
						robot.keyPress(KeyEvent.VK_R);
						robot.keyRelease(KeyEvent.VK_R);
						try { Thread.sleep(50); } catch (Exception e) {}
					} else if (g.type() == Type.TYPE_KEY_TAP) {
						robot.keyPress(KeyEvent.VK_D);
						robot.keyRelease(KeyEvent.VK_D);
						try { Thread.sleep(50); } catch (Exception e) {}
					}
			}
		} 
	}
	//}
}
public class LeapMouse {

	public static void main(String[] args) {
		CustomListener l = new CustomListener();
		Controller c = new Controller();
		c.addListener(l);
		try {
			System.in.read();
		} catch (Exception e) {}
		c.removeListener(l);
	}
}