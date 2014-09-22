package org.quickload.record;

public class RecordCursor
{
    private final PageAllocator allocator;
    private final int[] columnOffsets;
    private final int payloadOffset;

    private Page page;
    private int position;
    private int rowSize;
    private final byte[] nullBitSet;

    RecordCursor(PageAllocator allocator, Schema schema)
    {
        this.allocator = allocator;
        this.columnOffsets = Page.columnOffsets(schema);
        this.payloadOffset = Page.payloadOffset(schema);
        this.nullBitSet = new byte[Page.nullBitSetSize(schema)];
    }

    public void reset(Page page)
    {
        if (this.page != null) {
            allocator.releasePage(page);
            this.page = null;
        }
        this.page = page;
        this.position = 0;
        this.rowSize = 0;
    }

    public boolean next()
    {
        if (page == null) {
            return false;
        }

        position += rowSize;
        if (position < page.length()) {
            rowSize = page.getInt(position);
            page.getBytes(position + 4, nullBitSet, 0, nullBitSet.length);
            return true;
        }

        if (page != null) {
            allocator.releasePage(page);
            page = null;
        }
        return false;
    }

    public Page getPage()
    {
        return page;
    }

    public int getPayloadPosition(int dataOffset)
    {
        return position + payloadOffset + dataOffset;
    }

    public boolean isNull(int columnIndex)
    {
        return (nullBitSet[columnIndex >>> 3] & (1 << (columnIndex & 7))) != 0;
    }

    public byte getByte(int columnIndex)
    {
        return page.getByte(position + columnOffsets[columnIndex]);
    }

    public int getInt(int columnIndex)
    {
        return page.getInt(position + columnOffsets[columnIndex]);
    }

    public long getLong(int columnIndex)
    {
        return page.getLong(position + columnOffsets[columnIndex]);
    }

    public float getFloat(int columnIndex)
    {
        return page.getFloat(position + columnOffsets[columnIndex]);
    }

    public double getDouble(int columnIndex)
    {
        return page.getDouble(position + columnOffsets[columnIndex]);
    }

    public String getString(int columnIndex)
    {
        int index = page.getInt(position + columnOffsets[columnIndex]);
        return page.getStringReference(index);
    }
}