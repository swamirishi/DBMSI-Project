package heap;

import quadrupleheap.*;

public class BasicPatternClass {

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

    public BasicPatternClass(BasicPatternClass fromPattern)
    {
        data = fromPattern.getBasicPatternByteArray();
        basicPattern_length = fromPattern.getLength();
        basicPattern_offset = 0;
        fldCnt = fromPattern.noOfFlds();
        fldOffset = fromPattern.copyFldOffset();
    }

    public BasicPatternClass(Quadruple  tuple)
    {

        data = new byte[max_size];
        basicPattern_offset = 0;
        basicPattern_length = max_size;

        try
        {
            int no_tuple_fields = 4;
            setHdr((short)(7);
            int j = 1;
            for(int i = 1; i < fldCnt; i++) {

                int slotno = tuple.getIntFld(j++);
                int pageno = tuple.getIntFld(j++);



            }

        }
        catch(Exception e)
        {
            System.out.println("Error creating basic pattern from tuple"+e);
        }
    }






}







