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
import java.io.File;

public class SoundFile implements SoundObject {
	
	PApplet parent;
	private Engine m_engine;
	private MethClaInterface methCla;
	private int[] m_nodeId = {-1,-1};
	int[] m_info;
	String m_filePath;
	float m_rate=1; 
	float m_amp=1; 
	float m_add=0; 
	int m_cue=0;
	float m_pos=0; 
	boolean m_loop;
	int m_panBusId;	
	
	public SoundFile(PApplet theParent, String path) {
		this.parent = theParent;
		parent.registerMethod("dispose", this);
		m_engine.setPreferences(theParent, 512, 44100);
    	m_engine.start();
    	methCla = new MethClaInterface();
		m_filePath=theParent.dataPath(path);
		File sample = new File(m_filePath);
		if(sample.isFile() == true){
			m_info = m_engine.soundFileInfo(m_filePath);
		}
		else {
			System.out.println("Error: Soundfile doesn't exist. Pleae check path");
		}
		//m_panBusId = m_engine.soundFileConstructMono();
		m_panBusId = m_engine.busConstructMono();
	}
	
	public int frames(){
		return (int)m_info[0];
	}

	public int sampleRate(){
		return (int)m_info[1];
	}

	public int channels(){
		return (int)m_info[2];
	}
	
	public float duration(){
		return (float) this.frames()/this.sampleRate();
	}
	
	public void play(){
		m_loop=false;
		if(this.channels() == 1){
			m_nodeId = methCla.soundFilePlayMono(m_rate, m_pos, m_amp, m_add, false, m_filePath, this.duration()*(1/m_rate), m_cue, m_panBusId);
		}
		else if(this.channels() == 2){
			m_nodeId = methCla.soundFilePlayMulti(m_rate, m_amp, m_add, false, m_filePath, this.duration()*(1/m_rate), m_cue);
		}
	}

	public void play(float rate, float pos, float amp, float add, int cue){
		m_rate=rate; m_pos=pos; m_amp=amp; m_add=add; m_cue=(int)cue * m_info[1];
		this.play();
	}
	
	public void play(float rate, float pos, float amp, float add){
		m_rate=rate; m_pos=pos; m_amp=amp; m_add=add;
		this.play();
	}
	
	public void play(float rate, float pos, float amp){
		m_rate=rate; m_pos=pos; m_amp=amp;
		this.play();
	}
	
	public void play(float rate, float amp){
		m_rate=rate; m_amp=amp;
		this.play();
	}

	public void play(float rate){
		m_rate=rate;
		this.play();
	}
	
	public void loop(){
		m_loop=true;
		if(this.channels() < 2){
			m_nodeId = methCla.soundFilePlayMono(m_rate, m_pos, m_amp, m_add, true, m_filePath, this.duration()*(1/m_rate), m_cue, m_panBusId);
		}
		else if(this.channels() == 2){
			m_nodeId = methCla.soundFilePlayMulti(m_rate, m_amp, m_add, true, m_filePath, this.duration()*(1/m_rate), m_cue);
		}
	}

	public void loop(float rate, float pos, float amp, float add, int cue){
		m_rate=rate; m_pos=pos; m_amp=amp; m_add=add; m_cue=cue;
		this.loop();
	}
	
	public void loop(float rate, float pos, float amp, float add){
		m_rate=rate; m_pos=pos; m_amp=amp; m_add=add;
		this.loop();
	}
	
	public void loop(float rate, float pos, float amp){
		m_rate=rate; m_pos=pos; m_amp=amp;
		this.loop();
	}
	
	public void loop(float rate, float amp){
		m_rate=rate; m_amp=amp;
		this.loop();
	}

	public void loop(float rate){
		m_rate=rate;
		this.loop();
	}
    
    public void jump(float time){
        
        if(m_nodeId[0]>(-1)){
            this.stop();
        }
        
		m_cue = (int)(time * m_info[1]);
        
        if(m_loop == true) {
        	if(this.channels() < 2){
				m_nodeId = methCla.soundFilePlayMono(m_rate, m_pos, m_amp, m_add, true, m_filePath, this.duration()*(1/m_rate), m_cue, m_panBusId);
			}
			else if(this.channels() == 2){
				m_nodeId = methCla.soundFilePlayMulti(m_rate, m_amp, m_add, true, m_filePath, this.duration()*(1/m_rate), m_cue);
			}
        }
        else {
  			if(this.channels() < 2){
				m_nodeId = methCla.soundFilePlayMono(m_rate, m_pos, m_amp, m_add, false, m_filePath, this.duration()*(1/m_rate), m_cue, m_panBusId);
			}
			else if(this.channels() == 2){
				m_nodeId = methCla.soundFilePlayMulti(m_rate, m_amp, m_add, false, m_filePath, this.duration()*(1/m_rate), m_cue);
			}	
        }
	}
	
	public void cue(float time){
		m_cue = (int)(time * m_info[1]);
	}
	
	private void set(){
		if(m_nodeId[0] != -1 ) {
			if(this.channels() < 2){
				m_engine.soundFileSetMono(m_rate, m_pos, m_amp, m_add, m_nodeId);		
			}
			else if(this.channels() == 2){
				m_engine.soundFileSetStereo(m_rate, m_amp, m_add, m_nodeId);
			}	
		}	
	}
	
	public void set(float rate, float pos, float amp, float add){
		m_rate=rate;m_pos=pos;m_amp=amp;m_add=add;
		this.set();
	}
	
	public void pan(float pos){
		if(this.channels() > 1){
			throw new UnsupportedOperationException("Panning is not supported for stereo files");
		}
		
		m_pos=pos;
		this.set();
	}

	public void rate(float rate){
		m_rate=rate;
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

	public void stop(){
		m_engine.synthStop(m_nodeId);
	}
	
	public int[] returnId(){
		return m_nodeId;
	}

	public void dispose() {
		m_engine.synthStop(m_nodeId);
	}
}
