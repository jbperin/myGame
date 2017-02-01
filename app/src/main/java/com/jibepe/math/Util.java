package com.jibepe.math;

import static java.lang.Math.abs;

/**
 * Created by tbpk7658 on 01/02/2017.
 * http://geomalgorithms.com/a06-_intersect-2.html
 */
public class Util {
    final private static String TAG = "Util";
    final private static float SMALL_NUM = 0.00000001f ;

    public static float [] intersect3D_RayTriangle( float [] rayPoint, float [] rayDirection , float [] triVertex0, float [] triVertex1, float [] triVertex2) {

//        #define dot(u,v)   ((u).x * (v).x + (u).y * (v).y + (u).z * (v).z)
//
//
//
//// intersect3D_RayTriangle(): find the 3D intersection of a ray with a triangle
////    Input:  a ray R, and a triangle T
////    Output: *I = intersection point (when it exists)
////    Return: -1 = triangle is degenerate (a segment or point)
////             0 =  disjoint (no intersect)
////             1 =  intersect in unique point I1
////             2 =  are in the same plane
//int
//        intersect3D_RayTriangle( Ray R, Triangle T, Point* I )
//        {
//            Vector    u, v, n;              // triangle vectors
//            Vector    dir, w0, w;           // ray vectors
//            float     r, a, b;              // params to calc ray-plane intersect
//
//            // get triangle edge vectors and plane normal
//            u = T.V1 - T.V0;
        Vector U = (new Vector(triVertex1) ).minus(new Vector(triVertex0));
//            v = T.V2 - T.V0;
        Vector V = (new Vector(triVertex2) ).minus(new Vector(triVertex0));
//            n = u * v;              // cross product
        Vector N = U.cross(V);
//            if (n == (Vector)0)             // triangle is degenerate
//                return -1;                  // do not deal with this case
        if (N.magnitude() < SMALL_NUM) {
            return null;
        }
//
//            dir = R.P1 - R.P0;              // ray direction vector
        Vector dir = new Vector (rayDirection);
//            w0 = R.P0 - T.V0;
        Vector w0 = (new Vector(rayPoint)).minus(new Vector(triVertex0));
//            a = -dot(n,w0);
        float a = - N.dot(w0);
//            b = dot(n,dir);*
        float b = N.dot(dir);
//            if (fabs(b) < SMALL_NUM) {     // ray is  parallel to triangle plane
//                if (a == 0)                 // ray lies in triangle plane
//                    return 2;
//                else return 0;              // ray disjoint from plane
//            }
        if ((abs(b) < SMALL_NUM) || (abs(a) < SMALL_NUM)) {

            return null;
        }
//
//            // get intersect point of ray with triangle plane
//            r = a / b;
        float r = a / b;
//            if (r < 0.0)                    // ray goes away from triangle
//                return 0;                   // => no intersect
        if (r < 0.0){
            return null;
        }
//            // for a segment, also test if (r > 1.0) => no intersect
//
//    *I = R.P0 + r * dir;            // intersect point of ray and plane
        Vector I = new Vector (new float [] {
            rayPoint[0] +  r* rayDirection[0]
                    , rayPoint[1] +  r* rayDirection[1]
                    , rayPoint[2] +  r* rayDirection[2] });
//
//            // is I inside T?
//            float    uu, uv, vv, wu, wv, D;
//            uu = dot(u,u);
        float uu = U.dot(U);
//            uv = dot(u,v);
        float uv = U.dot(V);
//            vv = dot(v,v);
        float vv = V.dot(V);
//            w = *I - T.V0;
        Vector w = I.minus(new Vector (triVertex0));
//            wu = dot(w,u);
        float wu = w.dot(U);
//            wv = dot(w,v);
        float wv = w.dot(V);
//            D = uv * uv - uu * vv;
        float D = uv * uv - uu * vv;
//
//            // get and test parametric coords
//            float s, t;
//            s = (uv * wv - vv * wu) / D;
        float s = (uv * wv - vv * wu) / D;
//            if (s < 0.0 || s > 1.0)         // I is outside T
//                return 0;
        if (s < 0.0 || s > 1.0) {
            return null;
        }
//            t = (uv * wu - uu * wv) / D;
        float t = (uv * wu - uu * wv) / D;
//            if (t < 0.0 || (s + t) > 1.0)  // I is outside T
//                return 0;
        if (t < 0.0 || (s + t) > 1.0) {
            return null;
        }

//
//            return 1;                       // I is in T


        return (I.array());
    }

}
