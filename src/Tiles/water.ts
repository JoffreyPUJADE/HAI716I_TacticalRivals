import { Unit } from "../Units/unit";
import { Tile } from "./tile";

export class Water implements Tile {
    defense: number;
    obstacle: boolean;
    occupiedBy?: Unit | undefined;
    
    constructor() {
        this.defense = 0;
        this.obstacle = true;
        this.occupiedBy = undefined;
    }
}