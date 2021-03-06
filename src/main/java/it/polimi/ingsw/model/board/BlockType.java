package it.polimi.ingsw.model.board;

/**
 * This enum is used to represent the level of a block, each block will have its own "type" from one of the following:
 * TERRAIN :- 0, LEVEL1 :- 1, LEVEL2 :- 2, LEVEL3 :- 3 or DOME :- 4
 */
public enum BlockType {
    TERRAIN(0),
    LEVEL1(1),
    LEVEL2(2),
    LEVEL3(3),
    DOME(4);

    private final int level;

    /**
     * This is the constructor for the BlockType enum.
     * @param level This is the type of the block  (1°floor->1| 2 | 3 |4 <- DOME).
     */
    BlockType(int level){
        this.level=level;
    }

    /**
     * This function is used to retrieve the level of the block selected.
     * @return byte This is the level of the block selected.
     */
    public int getLevel(){
        return this.level;
    }

    /**
     * This function is used to retrieve the BlockType of the block above the block selected.
     * @return BlockType This is the BlockType of the block above the block selected.
     */
    public BlockType getLevelAbove() {
        BlockType blockTypeAbove = BlockType.TERRAIN;
        switch (this) {
            case TERRAIN:
                blockTypeAbove =  BlockType.LEVEL1;
                break;
            case LEVEL1:
                blockTypeAbove =  BlockType.LEVEL2;
                break;
            case LEVEL2:
                blockTypeAbove =  BlockType.LEVEL3;
                break;
            case LEVEL3:
                blockTypeAbove = BlockType.DOME;
        }
        return blockTypeAbove;
    }
}
