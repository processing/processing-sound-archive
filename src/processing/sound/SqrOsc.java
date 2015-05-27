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

public class SqrOsc implements SoundObject {
		
	PApplet parent;
	private Engine m_engine;
	private int[] m_nodeId = {-1,-1};
	private float m_freq = 440;
	private float m_amp = 0.5f;
	private float m_add = 0;
	private float m_pos = 0;
	private int m_panBusId;
	
	public SqrOsc(PApplet theParent) {
		this.parent = theParent;
		parent.registerMethod("dispose", this);
		m_engine.setPreferences(theParent, 512, 44100);
    	m_engine.start();
		m_panBusId = m_engine.busConstructMono();
	}
	
	public void play(){
		//m_nodeId = m_engine.pulsePlay(m_freq, 0.5f, m_amp*2, m_add-1, m_pos);
		m_nodeId = m_engine.sqrPlay(m_freq, m_amp, m_add-1, m_pos, m_panBusId);
	};	
	
	public void play(float freq, float amp, float add, float pos){
		m_freq=freq; m_amp=amp; m_add=add; m_pos=pos;
		this.play();
	};
	
	public void play(float freq, float amp, float add){
		m_freq=freq; m_amp=amp; m_add=add;
		this.play();
	};
	
	public void play(float freq, float amp){
		m_freq=freq; m_amp=amp;
		this.play();
	};
	
	public void play(float freq){
		m_freq=freq; 
		this.play();
	};
	
	private void set(){
		if(m_nodeId[0] != -1 ) {
			m_engine.sqrSet(m_freq, m_amp, m_add-1, m_pos, m_nodeId);
		}	
	}
	
	public void set(float freq, float amp, float add, float pos){
		m_freq=freq; m_amp=amp; m_add=add; m_pos=pos;
		this.set();
	};
	
	public void freq(float freq){
		m_freq=freq;
		this.set();		
	}
	
	public void amp(float amp){
		m_amp=amp;
		this.set();
	}
	
	public void add(float add){
		m_add=add;
		this.set();
	}
	
	public void pan(float pos){
		m_pos=pos;
		this.set();
	}
	
	public void stop(){
		m_engine.synthStop(m_nodeId);
	};
	
	public int[] returnId(){
		return m_nodeId;
	};
	
	public void dispose(){
		m_engine.synthStop(m_nodeId);
	};
}


