uniform    mat4        u_MVPMatrix;
uniform    vec4		   u_Color;			// This will be passed into the fragment shader.

attribute  vec4        a_Position;

varying vec4 v_Color;			// This will be passed into the fragment shader.
          		
void main() {

	v_Color = u_Color;

	gl_Position = u_MVPMatrix * a_Position;
}