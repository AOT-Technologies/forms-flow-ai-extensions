/**
 * Deeply clone a value
 * Source code from: https://github.com/marvinhagemeister/smoldash
 */
export function cloneDeep<T>(value: T): T {
  if (Array.isArray(value)) {
    return value.slice().map(cloneDeep) as any;
  }
  
  else if (value instanceof RegExp) {
    return new RegExp(value.source, value.flags) as any;
  }
  else if (value instanceof Set) {
    const out = new Set();
    value.forEach(v => out.add(cloneDeep(v)));
    return out as any;
  }
  else if (value instanceof Map) {
    const out = new Map();
    value.forEach((v, k) => out.set(k, cloneDeep(v)));
    return out as any;
  }
  else if (value instanceof Date) {
    return new Date(value) as any;
  }
  else if (typeof value === "object" && value !== null) {
    const out: Record<string, any> = {
    };
    for (const k in value) {
      out[k] = cloneDeep(value[k]);
    }
    return out as any;
  }

  return value;
}

/**
 * Shallowly compare to values
 * Source code from: https://github.com/marvinhagemeister/smoldash
 */
export function isEqual<T>(a: T, b: T): boolean {
  if (a === null || b === null) {
    return a === b;
  } 
  
  else if (Array.isArray(a) && Array.isArray(b)) {
    if (a.length !== b.length) {
      return false;
    }

    for (let i = 0; i < a.length; i++) {
      if (a[i] !== b[i]) {
        return false;
      }
    }

    return true;
  }
  
  else if (a instanceof RegExp && b instanceof RegExp) {
    return "" + a === "" + b;
  }
  //  else if (a instanceof Set && b instanceof Set) {
  //   if (a.size !== b.size) return false;

  //   for (const v of a.values()) {
  //     if (!b.has(v)) return false;
  //   }
  //   return true;
  // } else if (a instanceof Map && b instanceof Map) {
  //   if (a.size !== b.size) return false;

  //   for (const [k, v] of a.entries()) {
  //     if (!b.has(k)) return false;
  //     if (b.get(k) !== v) return false;
  //   }
  //   return true;
  // }
  else if (a instanceof Date && b instanceof Date) {
    return +a === +b;
  } 
  
  else if (typeof a === "object" && typeof b === "object") {
    for (const i in a) if (!(i in b)) return false;
    for (const i in b) if (a[i] !== b[i]) return false;
    return true;
  }

  return a === b;
}
