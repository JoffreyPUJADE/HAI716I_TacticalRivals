import { Unit } from "./unit";

export class Artillery implements Unit {
    health: number;
    power: number;
    armor: number;
    speed: number;

    constructor() {
        this.health = 10;
        this.power = 8;
        this.armor = 4;
        this.speed = 6;
    }
}