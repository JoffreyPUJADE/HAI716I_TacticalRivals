/**
 * Représente une unité qui sera contrôlable par les agents.
 */
export interface Unit {
    /**
     * Santé de l'unité (maximum 10)
     */
    health: number,
    /**
     * Puissance d'attaque de l'unité 
     */
    power: number,
    /**
     * Défense de l'unité
     */
    armor: number,
    /**
     * Nombre de case maximum de déplacement
     */
    speed: number,
    /**
     * Portée d'attaque de l'unité
     */
    range: number
}

