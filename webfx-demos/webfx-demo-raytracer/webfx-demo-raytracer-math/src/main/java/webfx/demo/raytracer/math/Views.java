package webfx.demo.raytracer.math;

/**
 * @author Bruno Salmon
 */
public final class Views {

    private static int objectIterator = 0;
    private static int lightIterator = 0;

    private final static Floor floor;
    private final static View view;
    private final static View materialView;
    private final static View lightView;
    private final static Sphere rmFloor;

    static {
        Materials.materials = new Material[] {
                Materials.Default,
                Materials.Floor,
                Materials.Mirror,
                Materials.Glass
        };

        floor = new Floor() {{
            material = Materials.Floor;
            objectPosition = Vector.create(0, 1, 0);
            displacement = 0;
            name = "Floor";
        }};

        rmFloor = new Sphere() {{
            material = Materials.Floor;
            objectPosition = Vector.create(0, -10000, 0);
            radius = 0.0000001;
            name = "Floor";
        }};

        view = new View()
        {{
            objects = new AbsObject[] {
                    floor,
                    new Sphere() {{
                        material = Materials.Glass;
                        objectPosition = Vector.create(0, 3, 1);
                        radius = 1.0;
                        name = "Sphere." + objectIterator;
                        objectIterator++;
                    }},
                    new Sphere() {{
                        material = Materials.Glass;
                        objectPosition = Vector.create(4, 3, 1);
                        radius = 1.5;
                        name = "Sphere." + objectIterator;
                        objectIterator++;
                    }},
                    new Sphere() {{
                        material = Materials.Mirror;
                        objectPosition = Vector.create(0, 1, 0);
                        radius = 0.5;
                        name = "Sphere." + objectIterator;
                        objectIterator++;
                    }},
                    new Sphere() {{
                        material = Materials.Mirror;
                        objectPosition = Vector.create(-4, 5, 2);
                        radius = 2.0;
                        name = "Sphere." + objectIterator;
                        objectIterator++;
                    }},
                    new Sphere() {{
                        material = Materials.Mirror;
                        objectPosition = Vector.create(3, 2, 6);
                        radius = 1.75;
                        name = "Sphere." + objectIterator;
                        objectIterator++;
                    }},
                    new Sphere() {{
                        material = Materials.Mirror;
                        objectPosition = Vector.create(-5, 5, -6);
                        radius = 4.0;
                        name = "Sphere." + objectIterator;
                        objectIterator++;
                    }}
            };
            lightSource = new LightSource[] {
                    new LightSource() {{
                        color = Colour.create(0.5, 0.3, 0.75);
                        position = Vector.create(-2, 5, 0);
                        name = "Light." + lightIterator;
                        lightIterator++;
                    }},
                    new LightSource() {{
                        color = Colour.create(0.0, 0.8, 0.8);
                        position = Vector.create(-10, 12, -7.5);
                        name = "Light." + lightIterator;
                        lightIterator++;
                    }},
/*
                    new LightSource() {{
                        color = Colour.create(0.5, 0.6, 0.3);
                        position = Vector.create(-6, 8, 5);
                        name = "Light." + lightIterator;
                        lightIterator++;
                    }},
*/
                    new LightSource() {{
                        color = Colour.create(0.5, 0.1, 0.1);
                        position = Vector.create(4, 2.5, -5);
                        name = "Light." + lightIterator;
                        lightIterator++;
                    }}};
            camera = Camera.create(Vector.create(0, 1, 0), Vector.create(5, 5, 5));
        }};

        materialView = new View(){{
            objects = new AbsObject[]{
                    new Sphere() {{
                        material = Materials.Mirror;
                        objectPosition = Vector.create(0, 1, 0);
                        radius = 1.0;
                        name = "Sphere";
                    }},
                    floor
            };
            lightSource = new LightSource[] {
                    new LightSource() {{
                        color = Colour.create(0.75, 0.75, 0.75);
                        position = Vector.create(4, 3, 4);
                        name = "Light";
                    }},
                    new LightSource() {{
                        color = Colour.create(0.75, 0.75, 0.75);
                        position = Vector.create(4, 3, -4);
                        name = "Light";
                    }},
                    new LightSource() {{
                        color = Colour.create(0.75, 0.75, 0.75);
                        position = Vector.create(-4, 3, 4);
                        name = "Light";
                    }},
                    new LightSource() {{
                        color = Colour.create(0.75, 0.75, 0.75);
                        position = Vector.create(-4, 3, -4);
                        name = "Light";
                    }}
            };
            camera = Camera.create(Vector.create(0, 1, 0), Vector.create(3, 3, 3), 600, 450);
        }};

        lightView = new View(){{
            objects = new AbsObject[]{
                    floor
            };
            lightSource = new LightSource[] {
                    new LightSource() {{
                        color = Colour.create(1.0, 1.0, 1.0);
                        position = Vector.create(0, 1, 0);
                        name = "Light";
                    }}
            };
            camera = Camera.create(Vector.create(0, 1, 0), Vector.create(3, 3, 3), 600, 450);
        }};
    }

    public static View[] VIEWS = {view};
}
