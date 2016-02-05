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

public class Reverb implements SoundObject{
	
	PApplet parent;
	private Engine m_engine;
	private int[] m_nodeId = {-1, -1};
	private float m_room = 1;
	private float m_damp = 0;
	private float m_wet = 0.5f;
	
	public Reverb(PApplet theParent) {
		this.parent = theParent;
		parent.registerMethod("dispose", this);
		m_engine.setPreferences(theParent, 512, 44100);
    	m_engine.start();
   	}
	
	public void process(SoundObject input, float room, float damp, float wet){
		m_room=room; m_damp=damp; m_wet=wet;
		m_nodeId = m_engine.reverbPlay(input.returnId(), m_room, m_damp, m_wet);
	}
	
	public void process(SoundObject input, float room, float damp){
		m_room=room; m_damp=damp;
		m_nodeId = m_engine.reverbPlay(input.returnId(), m_room, m_damp, m_wet);
	}

	public void process(SoundObject input, float room){
		m_room=room;
		m_nodeId = m_engine.reverbPlay(input.returnId(), m_room, m_damp, m_wet);
	}

	/**
	* Start the reverb effect.
	* @webref sound
	* @param input The input audio signal.
	**/

	public void process(SoundObject input){
		m_nodeId = m_engine.reverbPlay(input.returnId(), m_room, m_damp, m_wet);
	}	
	
	private void set(){
		if(m_nodeId[0] != -1 ) {
			m_engine.reverbSet(m_room, m_damp, m_wet, m_nodeId[0]);
		}
	}
	
	/**
	* Set multiple parameters at once
	* @webref sound
	* @param room A value controlling the room size of the effet
	* @param damp A value controlling the dampening factor of the reverb
	* @param wet A value controlling the wet/dry ratio of the reverb.
	**/

	public void set(float room, float damp, float wet){
		m_room=room; m_damp=damp; m_wet=wet;
		this.set();
	}
	
	/**
	* Change the room size of the reverb effect.
	* @webref sound
	* @param room A float value controlling the room size of the effect.
	**/

	public void room(float room){
		m_room=room;
		this.set();
	}

	/**
	* Change the dampening of the reverb effect
	* @webref sound
	* @param damp A float value controlling the dampening factor of the reverb
	**/

	public void damp(float damp){
		m_damp=damp;
		this.set();
	}
	
	/**
	* Change the dry/wet ratio of the delay effect
	* @webref sound
	* @param wet A float value controlling the wet/dry ratio of the reverb.
	**/

	public void wet(float wet){
		m_wet=wet;
		this.set();
	}

	public int[] returnId(){
		return m_nodeId;
	}
	
	/**
	* Stop the reverb effect
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
