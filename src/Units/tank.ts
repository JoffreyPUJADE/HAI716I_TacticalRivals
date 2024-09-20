import { Unit } from "./unit";

class Tank implements Unit {
    health: number;
    power: number;
    armor: number;
    speed: number;

    constructor() {
        this.health = 10;
        this.power = 6;
        this.armor = 7;
        this.speed = 3;
    }
}