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
* This is a simple Saw Wave Oscillator 
* @webref sound
* @param parent PApplet: typically use "this"
**/

public class SawOsc implements Oscillator{
	
	PApplet parent;
	private Engine m_engine;
	private int[] m_nodeId = {-1,-1};
	
	private int[] m_freqId = {-1,-1};
	private int[] m_ampId = {-1,-1};
	private int[] m_addId = {-1,-1};
	private int[] m_posId = {-1,-1};
		
	private float m_freq = 440;
	private float m_amp = 0.5f;
	private float m_add = 0;
	private float m_pos = 0;
	private int m_panBusId;	
	
	public SawOsc(PApplet theParent) {
		this.parent = theParent;
		parent.registerMethod("dispose", this);
		m_engine.setPreferences(theParent, 512, 44100);
    	m_engine.start();		
		m_panBusId = m_engine.busConstructMono();    	
	}
	
	public void play(float freq, float amp, float add, float pos){
		m_freq=freq; m_amp=amp; m_add=add; m_pos=pos;
		m_nodeId = m_engine.sawPlay(m_freq, m_amp, m_add, m_pos, m_panBusId);
	}
	
	public void play(float freq, float amp, float add){
		m_freq=freq; m_amp=amp; m_add=add;
		m_nodeId = m_engine.sawPlay(m_freq, m_amp, m_add, m_pos, m_panBusId);
	}
	
	public void play(float freq, float amp){
		m_freq=freq; m_amp=amp;
		m_nodeId = m_engine.sawPlay(m_freq, m_amp, m_add, m_pos, m_panBusId);
	}
	
	/**
	* Starts the oscillator
	* @webref sound
	**/
	
	public void play(){
		m_nodeId = m_engine.sawPlay(m_freq, m_amp, m_add, m_pos, m_panBusId);
	}
	
	private void set(){
		if(m_nodeId[0] != -1 ) {
			m_engine.oscSet(m_freq, m_amp, m_add, m_pos, m_nodeId);
		}	
	}

	private void audioSet(){
		if(m_nodeId[0] != -1 ) {
			m_engine.oscAudioSet(m_freqId, m_ampId, m_addId, m_posId, m_nodeId);
		}	
	}
	
	/**
	* Set multiple parameters at once
	* @webref sound
	* @param freq The frequency value of the oscillator in Hz.
	* @param amp The amplitude of the oscillator as a value between 0.0 and 1.0.
	* @param add A value for modulating other audio signals.
	* @param pos The panoramic position of the oscillator as a float from -1.0 to 1.0.
	**/

	public void set(float freq, float amp, float add, float pos){
		m_freq=freq; m_amp=amp; m_add=add; m_pos=pos;
		this.set();
	}
	
	/**
	* Set the freuquency of the oscillator in Hz.
	* @webref sound
	* @param freq A floating point value of the oscillator in Hz.
	**/

	public void freq(float freq){
		m_freq=freq;
		this.set();
	}

	public void freq(SoundObject freq){
		m_freqId=freq.returnId();
		this.audioSet();
	}	

	/**
	* Set the amplitude/volume of the oscillator
	* @webref sound
	* @param amp The amplitude value of the oscillator as a float fom 0.0 to 1.0
	**/

	public void amp(float amp){
		m_amp=amp;
		this.set();
	}

	public void amp(SoundObject amp){
		m_ampId=amp.returnId();
		this.audioSet();
	}	

	/**
	* Offset the output of the oscillator by given value
	* @webref sound
	* @param add A value for modulating other audio signals.
	**/	

	public void add(float add){
		m_add=add;
		this.set();
	}
	
	public void add(SoundObject add){
		m_addId=add.returnId();
		this.audioSet();
	}	

	/**
	* Move the sound in a stereo panorama
	* @webref sound
	* @param pos The panoramic position of the oscillator as a float from -1.0 to 1.0.
	**/	

	public void pan(float pos){
		m_pos=pos;
		this.set();
	}
	
	public void pan(SoundObject pos){
		m_posId=pos.returnId();
		this.audioSet();
	}

	/**
	* Stop the oscillator.
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
