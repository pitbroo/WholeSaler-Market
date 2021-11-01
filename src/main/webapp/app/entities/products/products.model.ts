export interface IProducts {
  id?: number;
  name?: string | null;
  price?: number | null;
  description?: string | null;
}

export class Products implements IProducts {
  constructor(public id?: number, public name?: string | null, public price?: number | null, public description?: string | null) {}
}

export function getProductsIdentifier(products: IProducts): number | undefined {
  return products.id;
}
