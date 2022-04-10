package basicpattern;

import quadrupleheap.*;
import global.*;
import java.io.*;
import java.lang.*;

import quadrupleheap.*;

import static global.GlobalConst.MINIBASE_PAGESIZE;

public class BasicPatternClass implements GlobalConst{

    public static SystemDefs sysdef = null;

    public static final int max_size = MINIBASE_PAGESIZE;
    private byte [] data;
    private int basicPattern_offset;
    private int basicPattern_length;
    private short fldCnt;
    private short [] fldOffset;

    public  BasicPatternClass()
    {

        // Creat a new tuple
        data = new byte[max_size];
        basicPattern_offset = 0;
        basicPattern_length = max_size;
    }

    public BasicPatternClass(byte [] apattern, int offset, int length)
    {
        data = apattern;
        basicPattern_offset = offset;
        basicPattern_length = length;
        //  fldCnt = getShortValue(offset, data);
    }


    public BasicPatternClass(BasicPatternClass fromPattern)
    {
        data = fromPattern.getBasicPatternByteArray();
        basicPattern_length = fromPattern.getLength();
        basicPattern_offset = 0;
        fldCnt = fromPattern.noOfFlds();
        fldOffset = fromPattern.copyFldOffset();
    }

    public int getLength()
    {
        return basicPattern_length;
    }

    public short size()
    {
        return ((short) (fldOffset[fldCnt] - basicPattern_offset));
    }


    public int getOffset()
    {
        return basicPattern_offset;
    }

    public byte [] getBasicPatternByteArray()
    {
        byte [] patterncopy = new byte [basicPattern_length];
        System.arraycopy(data, basicPattern_offset, patterncopy, 0, basicPattern_length);
        return patterncopy;
    }

    public short noOfFlds()
    {
        return fldCnt;
    }
    public short[] copyFldOffset()
    {
        short[] newFldOffset = new short[fldCnt + 1];
        for (int i=0; i<=fldCnt; i++) {
            newFldOffset[i] = fldOffset[i];
        }

        return newFldOffset;
    }
    
    public BasicPatternClass(Quadruple  tuple)
    {

        data = new byte[max_size];
        basicPattern_offset = 0;
        basicPattern_length = max_size;

        try
        {
//            int no_tuple_fields = 4;
//            setHdr((short)(7));
            int j = 1;
            for(int i = 1; i < fldCnt; i++) {

//                int slotno = tuple.getIntFld(j++);
//                int pageno = tuple.getIntFld(j++);
//                EID eid =



            }

        }
        catch(Exception e)
        {
            System.out.println("Error creating basic pattern from tuple"+e);
        }
    }


    public void setHdr (short numFlds) throws InvalidBasicPatternSizeException, IOException
    {
        if((numFlds +2)*2 > max_size)
            throw new InvalidBasicPatternSizeException (null, "BASIC PATTERN: BASIC PATTERN_TOOBIG_ERROR");

        fldCnt = numFlds;
        Convert.setShortValue(numFlds, basicPattern_offset, data);
        fldOffset = new short[numFlds+1];
        int pos = basicPattern_offset+2;  // start position for fldOffset[]

        //sizeof short =2  +2: array siaze = numFlds +1 (0 - numFilds) and
        //another 1 for fldCnt
        fldOffset[0] = (short) ((numFlds +2) * 2 + basicPattern_offset);

        Convert.setShortValue(fldOffset[0], pos, data);
        pos +=2;
        short strCount =0;
        short incr;
        int i;

        for (i=1; i<numFlds; i++)
        {
            incr = 8;
            fldOffset[i]  = (short) (fldOffset[i-1] + incr);
            Convert.setShortValue(fldOffset[i], pos, data);
            pos +=2;

        }

        // For confidence
        incr = 8;

        fldOffset[numFlds] = (short) (fldOffset[i-1] + incr);
        Convert.setShortValue(fldOffset[numFlds], pos, data);

        basicPattern_length = fldOffset[numFlds] - basicPattern_offset;

        if(basicPattern_length > max_size)
            throw new InvalidBasicPatternSizeException (null, "BASIC PATTERN: BASIC PATTERN_TOOBIG_ERROR");
    }






}