package com.jibepe.objparser;

import java.nio.ByteBuffer;

public class MMesh extends MAbstractMesh {
    private ByteBuffer verts;
    private ByteBuffer textCoords;
    private ByteBuffer norms;
    private ByteBuffer mIndBuff;
    private int mMatId;
    int numVerts = 0;
    int numObjectIndex = 0;
    int numUVs = 0;
    int numNorms = 0;
    @Override
    public ByteBuffer getBuffer(BUFFER_TYPE bufferType)
    {
        ByteBuffer result = null;
        switch (bufferType)
        {
            case BUFFER_TYPE_VERTEX:
                result = verts;
                break;
            case BUFFER_TYPE_TEXTURE_COORD:
                result = textCoords;
                break;
            case BUFFER_TYPE_NORMALS:
                result = norms;
                break;
            case BUFFER_TYPE_INDICES:
                result = mIndBuff;
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public int getNumObjectVertex()
    {
        return numVerts;
    }

    @Override
    public int getNumObjectIndex()
    {
        return numObjectIndex;
    }
    public int getNumUVs() { return numUVs;}
    public int getNumNorms() { return numNorms;}

    public void setVerts(ByteBuffer Buff) { verts = Buff; numVerts = Buff.remaining()/12;}
    public void setUVs(ByteBuffer Buff) { textCoords = Buff; numUVs = Buff.remaining()/8;}
    public void setNorms(ByteBuffer Buff) { norms = Buff; numNorms = Buff.remaining()/12;}
    public void setIndices(ByteBuffer Buff) { mIndBuff = Buff; numObjectIndex = Buff.remaining()/2;}
    public void setMaterialId(int id) { mMatId = id;}
    public int getMaterialId() { return mMatId; }

}
