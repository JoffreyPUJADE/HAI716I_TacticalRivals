import {Unit} from '../Units/unit';

/**
 * Représente une case de la grille
 */
export interface Tile {
  /**
   * Bonus de défense pour les unités
   */
  defense: number;
  /**
   * Indique si cette case peut être traversée ou non.
   */
  obstacle: boolean;
  /**
   * Indique si la case est occupée par une unité ou non.
   */
  occupiedBy?: Unit;
}