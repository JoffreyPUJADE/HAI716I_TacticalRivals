import { Unit } from "../Units/unit";
import { Tile } from "./tile";

export abstract class Urban implements Tile {
    defense: number;
    obstacle: boolean;
    occupiedBy?: Unit | undefined;

    constructor() {
        this.defense = 3;
        this.obstacle = false;
        this.occupiedBy = undefined;
    }
}