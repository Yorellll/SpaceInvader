package GraphicalObject;

import com.jogamp.opengl.GL2;

import java.util.ArrayList;

public class Cube extends GraphicalObject {

    private ArrayList<Square> faces;

    public Cube(float pX, float pY, float pZ,
                float angX, float angY, float angZ,
                float scale,
                float r, float g, float b) {
        super(pX, pY, pZ, angX, angY, angZ, scale, r, g, b);
        faces = new ArrayList<Square>();
        faces.add(new Square(0, 0, 1, 0, 0, 0, 1, r, g, b));//Front face

        faces.add(new Square(0, 0, -1, 0, 0, 0, 1, r, g, b));//Back face

        faces.add(new Square(1, 0, 0, 0, 90, 0, 1, r, g, b));//Right face

        faces.add(new Square(-1, 0, 0, 0, -90, 0, 1, r, g, b));//Left face

        faces.add(new Square(0, 0, 0, 0, 0, 0, 1, r, g, b));//Top face

        faces.add(new Square(0, 0, 0, 0, 0, 0, 1, r, g, b));//Bottom face


    }

    public void display_normalized(GL2 gl) {
        for (Square face : faces) {
            face.display(gl);
        }
    }

}