//package com.OpenGl;
//
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.nio.FloatBuffer;
//
//import javax.microedition.khronos.opengles.GL10;
//
///**
// * Created by haozhong on 2017/7/21.
// */
//
//public class TextureRect {
//    int vcount; //顶点总数
//    int texid;
//    private FloatBuffer fbv;
//
//    public TextureRect(int texid, double startAngle){
//        this.texid = texid;
//
//        int clos  = 20; //列数
//        int rows = clos * 3 / 4; //行数
//        //顶点总数
//        vcount = clos * rows * 2 * 3;
//        float[] vertices = new float[vcount*3]; //xyz
//        //Float[][] vertices = new Float[3][vcount];//
//        int count = 0; //计数器
//        //每个格子的单位长度
//        float size = 1.5F/ clos;
//        //每个格子的弧度
//        double angleSpan = Math.PI * 4 /clos;
//        for(int j = 0; j<rows; j++){
//            for (int i =0; i<clos; i++){
//                float zsX = -size*clos / 2 + i*size;
//                float zsY = size*clos / 2 - i*size;
//                float zsZ =(float) (Math.sin(startAngle + i*angleSpan)*0.1f);
//                //高低差
//                float zsP =(float) (Math.sin(startAngle + (i + 1)*angleSpan)*0.1f);
//                //左上坐标
//                vertices[count++] = zsX;
//                vertices[count++] = zsY;
//                vertices[count++] = zsZ;
//
//                //左下坐标
//                vertices[count++] = zsX;
//                vertices[count++] = zsY-size;
//                vertices[count++] = zsZ;
//
//                //右上坐标
//                vertices[count++] = zsX-size;
//                vertices[count++] = zsY;
//                vertices[count++] = zsP;
//
//                //右上坐标
//                vertices[count++] = zsX-size;
//                vertices[count++] = zsY;
//                vertices[count++] = zsP;
//
//                //左下坐标
//                vertices[count++] = zsX;
//                vertices[count++] = zsY-size;
//                vertices[count++] = zsZ;
//
//                //右下坐标
//                vertices[count++] = zsX-size;
//                vertices[count++] = zsY-size;
//                vertices[count++] = zsP;
//
//            }
//        }
//       fbv =  gettextureBuffer(vertices);
//    }
//
//    public void drawSelf(GL10 gl){
//        gl.glVertexPointer(3 //每个顶点的坐标数量3 xyz
//                ,GL10.GL_FLOAT //顶点类型
//                ,0 //间隔
//                ,fbv);//顶点坐标数据
//        gl.glDrawArrays(GL10.GL_LINES,0,vcount);
//    }
//
//    public FloatBuffer gettextureBuffer(float[] vt){
//        //创建顶点缓冲
//        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vt.length * 4);
//        //字节顺序
//        byteBuffer.order(ByteOrder.nativeOrder());
//        FloatBuffer fbv = byteBuffer.asFloatBuffer();
//        //加入缓冲区
//        fbv.put(vt);
//        //设置缓冲区位置
//        fbv.position(0);
//        return fbv;
//    }
//}
