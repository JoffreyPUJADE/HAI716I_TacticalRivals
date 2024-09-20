import { Unit } from "./unit";

export class Artillery implements Unit {
    health: number;
    power: number;
    armor: number;
    speed: number;
    range: number;

    constructor() {
        this.health = 10;
        this.power = 6;
        this.armor = 2;
        this.speed = 6;
        this.range = 3;
    }
}