import { Factory } from "./Tiles/factory";
import { Tile } from "./Tiles/tile";
import { Urban } from "./Tiles/urban";
import { Infantry } from "./Units/infantry";
import { Unit } from "./Units/unit";

export class Player {

    units: Unit[];
    resources: number;

    constructor() {
        this.units = [];
        this.resources = 0;
    }

    attack(u1: Unit, u2: Unit) {
        return null;
    }

    move(u: Unit, t: Tile){
        return null;
    }

    capture(i: Infantry, u: Urban){
        return null;
    }

    generate(f: Factory, u: Unit){
        return null;
    }
}