package processing.sound;
import processing.core.*;

public class FFT {
	
	PApplet parent;
	private Engine m_engine;
	private long ptr;

	private int fftSize;

	public float[] spectrum;
	
	public FFT(PApplet theParent, int fftSize_) {
		this.parent = theParent;
		parent.registerMethod("dispose", this);
		fftSize = fftSize_;
		spectrum = new float[fftSize];
		m_engine.setPreferences(theParent, 512, 44100);
    m_engine.start();
  }
	
	public void input(SoundObject input){
		ptr = m_engine.fft(input.returnId(), fftSize);
	}

	public void analyze(float[] value){
		float[] m_value = m_engine.poll_fft(ptr);
		int num_samples = Math.min(value.length, m_value.length);
		for(int i=0; i<num_samples; i++){
				value[i] = m_value[i];
		}
	}

	public int size() {
		return fftSize;
	}
	
	public void analyze(){
		float[] m_value = m_engine.poll_fft(ptr);
		int num_samples = Math.min(spectrum.length, m_value.length);
		for(int i=0; i<num_samples; i++){
				spectrum[i] = m_value[i];
		}
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
		m_engine.destroy_fft(ptr);
		//m_engine.synthStop(m_nodeId);
	}
}
