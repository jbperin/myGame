precision mediump float;

varying vec4 v_Color;          	// This is the color from the vertex shader interpolated across the 
  								// triangle per fragment.
void main() {
  gl_FragColor = v_Color;
}