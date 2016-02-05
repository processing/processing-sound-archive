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
* AudioIn let's you grab the audio input from your soundcard.
* @webref sound
* @param parent PApplet: typically use "this"
* @param in Input Channel 
**/

public class AudioIn implements SoundObject{
	
	PApplet parent;
	private Engine m_engine;
	private int[] m_nodeId = {-1,-1};
	private float m_amp = 1.f;
	private float m_add = 0;
	private int m_in = 0;
	private float m_pos = 0;
	
	public AudioIn (PApplet theParent, int in) {
		this.parent = theParent;
		parent.registerMethod("dispose", this);
		m_engine.setPreferences(theParent, 512, 44100);
    	m_engine.start();
    	m_in = in;
   	}
	
	/**
   	* Starts the input stream.
	* @webref sound
   	**/

   	public void start(){
		m_nodeId = m_engine.audioInStart(m_amp, m_add, m_pos, m_in);
	}
	
	public void start(float amp, float add, float pos){
		m_amp=amp; m_add=add; m_pos=pos;
		this.start();
	}
	
	public void start(float amp, float add){
		m_amp=amp; m_add=add;
		this.start();
	}
	
	public void start(float amp){
		m_amp=amp;
		this.start();
	}

	/**
	* Start the Input Stream and route it to the Audio Hardware Output
	* @webref sound
	**/

	public void play(){
		if(m_nodeId[1] < 0){
			this.start();
		}
		m_engine.audioInPlay(m_nodeId[1]);
	}
	
	private void set(){
		if(m_nodeId[0] != -1 ) {		
			m_engine.audioInSet(m_amp, m_add, m_pos, m_nodeId);
		}
	}
	
		/**
	* Set multiple parameters at once.
	* @webref sound
	* @param amp Amplitude value from 0.0 to 1.0
	* @param add Offset value for modulating other audio signals
	* @param pos Pan value from -1.0 to 1.0
	**/

	public void set(float amp, float add, float pos){
		m_amp=amp; m_add=add; m_pos=pos;
		this.set();
	}

	/**
	* Change the amplitude/volume of the input steam.
	* @webref sound
	* @param amp Amplitude value from 0.0 to 1.0
	**/

	public void amp(float amp){
		m_amp=amp;
		this.set();
	}
	
	/**
	* Offset the output of the input stream by given value
	* @webref sound
	* @param add Offset value for modulating other audio signals
	**/

	public void add(float add){
		m_add=add;
		this.set();
	}
	
	/**
	* Move the sound in a stereo panorama
	* @webref sound
	* @param pos Pan value from -1.0 to 1.0
	**/
	
	public void pan(float pos){
		m_pos=pos;
		this.set();
	}
	
	/**
	* Stop the input stream.
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
	
	public int[] returnId(){
		return m_nodeId;
	}

	public void dispose() {
		m_engine.synthStop(m_nodeId);
	}
}
