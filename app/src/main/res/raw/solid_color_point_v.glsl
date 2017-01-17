uniform mat4 u_MVPMatrix; 
uniform float u_Size;   
uniform vec4 u_Color;
 		
attribute vec4 a_Position;
    	
varying vec4 v_Color;			// This will be passed into the fragment shader.          		
    		
void main()                   
{                             
   gl_Position = u_MVPMatrix  
               * a_Position;
               
   v_Color = u_Color;
                 
   gl_PointSize = u_Size;   
}