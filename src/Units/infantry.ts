import { Unit } from "./unit";

export class Infantry implements Unit {
    health: number;
    power: number;
    armor: number;
    speed: number;

    constructor() {
        this.health = 10;
        this.power = 5;
        this.armor = 5;
        this.speed = 5;
    }
}