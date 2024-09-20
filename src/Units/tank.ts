import { Unit } from "./unit";

export class Tank implements Unit {
    health: number;
    power: number;
    armor: number;
    speed: number;
    range: number;

    constructor() {
        this.health = 10;
        this.power = 7;
        this.armor = 7;
        this.speed = 3;
        this.range = 1;
    }
}