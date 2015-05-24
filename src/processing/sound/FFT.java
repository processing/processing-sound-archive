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

public class FFT {
	
	PApplet parent;
	private Engine m_engine;
	public int m_fftSize;
	private long ptr;

	public float[] spectrum;
	
	public FFT(PApplet theParent, int fftSize) {
		this.parent = theParent;
		parent.registerMethod("dispose", this);
		spectrum = new float[fftSize];
		m_fftSize = fftSize;
		m_engine.setPreferences(theParent, 512, 44100);
    	m_engine.start();
  	}
	
	public void input(SoundObject input){
		ptr = m_engine.fft(input.returnId(), m_fftSize);
	}

	public void analyze(float[] value){
		float[] m_value = m_engine.poll_fft(ptr);
		int num_samples = Math.min(value.length, m_value.length);
		for(int i=0; i<num_samples; i++){
			value[i] = m_value[i];
		}
	}

	public void analyze(){
		float[] m_value = m_engine.poll_fft(ptr);
		int num_samples = Math.min(spectrum.length, m_value.length);
		for(int i=0; i<num_samples; i++){
			spectrum[i] = m_value[i];
		}
	}

	public int size() {
		return m_fftSize;
	}
	

	/*
	public void stop(){
		m_engine.synthStop(m_nodeId);
	}
	
	public int returnId(){
		return m_nodeId;
	}
	*/
	public void dispose() {
		//m_engine.synthStop(m_nodeId);
		m_engine.destroy_fft(ptr);
	}
}
