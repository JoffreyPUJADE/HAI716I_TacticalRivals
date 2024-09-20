import { Unit } from "../Units/unit";
import { Tile } from "./tile";

export class Plain implements Tile {
    defense: number;
    obstacle: boolean;
    occupiedBy?: Unit | undefined;
    
    constructor() {
        this.defense = 1;
        this.obstacle = false;
        this.occupiedBy = undefined;
    }
}