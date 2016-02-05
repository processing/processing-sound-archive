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
* This is a low pass filter
* @sound webref
* @param parent PApplet: typically use "this"
**/

public class LowPass implements SoundObject{
	
	PApplet parent;
	private Engine m_engine;
	private int[] m_nodeId = {-1, -1};
	private float m_freq = 100;
	
	public LowPass(PApplet theParent) {
		this.parent = theParent;
		parent.registerMethod("dispose", this);
		m_engine.setPreferences(theParent, 512, 44100);
    	m_engine.start();
   	}
	
	public void process(SoundObject input, float freq){
		m_freq=freq;
		m_nodeId = m_engine.lowPassPlay(input.returnId(), m_freq);
	}
	
	/**
	* Start the low pass filter.
	* @webref sound
	* @param input The input sound source.
	**/

	public void process(SoundObject input){
		m_nodeId = m_engine.lowPassPlay(input.returnId(), m_freq);
	}
	
	private void set(){
		if(m_nodeId[0] != -1 ) {		
			m_engine.filterSet(m_freq, m_nodeId[0]);
		}
	}
	
	public void set(float freq){
		m_freq=freq; 
		this.set();
	}
	
	/**
	* Sets the cut off frequency for the filter
	* @webref sound
	* @param freq The frequency value as a float
	**/

	public void freq(float freq){
		m_freq=freq;
		this.set();
	}

	public int[] returnId(){
		return m_nodeId;
	}
	
	/**
	* Stops the low pass filter
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
