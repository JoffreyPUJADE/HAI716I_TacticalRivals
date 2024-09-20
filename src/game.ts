class Game
{
    private m_map : any;
    private m_players : any[];
    private m_numTurn : number;

    public constructor()
    {
        // Init m_map.
        // Init m_players.

        this.m_numTurn = 1;
    }

    public start() : number
    {
        while(1) // Condition de fin.
        {
            this.turn();
        }

        return 0;
    }

    public turn() : void
    {
        ++this.m_numTurn;
    }

    public giveGold() : void
    {
        // Lâchez la thunasse.
    }
}