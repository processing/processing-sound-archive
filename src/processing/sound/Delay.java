/*
  Processing Sound (c) 2013-2015 Wilm Thoben
  Part of the Processing project - http://processing.org

  Copyright (c) 2011-12 Ben Fry and Casey Reas

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
*/
  
package processing.sound;
import processing.core.PApplet;

/**
* This is a simple delay effect.
* @webref sound
* @param parent PApplet: typically use "this"	 
**/

public class Delay implements SoundObject{
	
	PApplet parent;
	private Engine m_engine;
	private int m_nodeId[] = {-1,-1};
	private float m_maxDelayTime = 2;
	private float m_delayTime = 0;
	private float m_feedBack = 0;
	
	public Delay(PApplet theParent) {
		this.parent = theParent;
		parent.registerMethod("dispose", this);
		m_engine.setPreferences(theParent, 512, 44100);
    	m_engine.start();
   	}
	
	public void process(SoundObject input, float maxDelayTime, float delayTime, float feedBack){
		m_maxDelayTime=maxDelayTime; m_delayTime=delayTime; m_feedBack=feedBack;
		m_nodeId = m_engine.delayPlay(input.returnId(), m_maxDelayTime, m_delayTime, m_feedBack);
	}
	
	public void process(SoundObject input, float maxDelayTime, float delayTime){
		m_maxDelayTime=maxDelayTime; m_delayTime=delayTime; 
		m_nodeId = m_engine.delayPlay(input.returnId(), m_maxDelayTime, m_delayTime, m_feedBack);
	}

	/**
	* Start the delay effect	
	* @webref sound
	* @param input Input audio source
	* @param maxDelayTime Maximum delay time as a float.
	**/

	public void process(SoundObject input, float maxDelayTime){
		m_maxDelayTime=maxDelayTime; 
		m_nodeId = m_engine.delayPlay(input.returnId(), m_maxDelayTime, m_delayTime, m_feedBack);
	}
	
	private void set(){
		if(m_nodeId[0] != -1 ) {
			m_engine.delaySet(m_delayTime, m_feedBack, m_nodeId[0]);
		}
	}
	
	/**
	* Set delay time and feedback values at once
	* @webref sound
	* @param delayTime Maximum delay time as a float
	* @param feedBack Feedback amount as a float
	**/

	public void set(float delayTime, float feedBack){
		m_delayTime=delayTime; m_feedBack=feedBack;
		this.set();
	}
	
	/**
	* Changes the delay time of the effect.
	* @webref sound
	* @param delayTime Maximum delay time as a float.
	**/

	public void time(float delayTime){
		m_delayTime=delayTime;
		this.set();
	}

	/**
	* Change the feedback of the delay effect.
	* @webref sound
	* @param feedBack Feedback amount as a float.
	**/

	public void feedback(float feedBack){
		m_feedBack=feedBack;
		this.set();
	}
	
	public int[] returnId(){
		return m_nodeId;
	}
	
	/**
	* Stop the delay effect.
	* @webref sound
	**/
	
	public void stop(){
		if(m_nodeId[0] != -1 ) {
			m_engine.synthStop(m_nodeId);
			for(int i = 0; i < m_nodeId.length; i++)  {
				m_nodeId[i] = -1;
			}
		}
	}

	public void dispose() {
		m_engine.synthStop(m_nodeId);
	}
}
