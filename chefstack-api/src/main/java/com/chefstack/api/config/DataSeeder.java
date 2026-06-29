package com.chefstack.api.config;

import com.chefstack.api.model.Categoria;
import com.chefstack.api.model.Dificultad;
import com.chefstack.api.model.Ingrediente;
import com.chefstack.api.model.Receta;
import com.chefstack.api.repository.CategoriaRepository;
import com.chefstack.api.repository.RecetaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// siembra datos de ejemplo si la BD esta vacia
@Component
@ConditionalOnProperty(name = "chefstack.seed", havingValue = "true")
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final CategoriaRepository categoriaRepository;
    private final RecetaRepository recetaRepository;

    public DataSeeder(CategoriaRepository categoriaRepository, RecetaRepository recetaRepository) {
        this.categoriaRepository = categoriaRepository;
        this.recetaRepository = recetaRepository;
    }

    @Override
    public void run(String... args) {
        if (categoriaRepository.count() > 0) {
            return;
        }
        log.info("Sembrando datos demo de ChefStack...");

        Categoria postres = cat("Postres", "Dulces y reposteria");
        Categoria principales = cat("Platos principales", "Comidas fuertes");
        Categoria bebidas = cat("Bebidas", "Jugos, cocteles e infusiones");
        Categoria ensaladas = cat("Ensaladas", "Frescas y ligeras");
        Categoria desayunos = cat("Desayunos", "Para empezar el dia");

        List<Receta> recetas = new ArrayList<>();

        recetas.add(receta("Brownie de chocolate", "Brownie humedo y muy chocolatoso",
                "1. Derrite el chocolate. 2. Mezcla con huevos y azucar. 3. Anade harina. 4. Hornea 25 min a 180C.",
                40, Dificultad.MEDIA, 8, "638852", 466, postres,
                ing("Chocolate amargo", "200", "g"), ing("Mantequilla", "150", "g"),
                ing("Azucar", "200", "g"), ing("Huevos", "3", "unidades"), ing("Harina", "100", "g")));

        recetas.add(receta("Cheesecake de fresa", "Tarta de queso cremosa con coulis de fresa",
                "1. Tritura las galletas con mantequilla y forma la base. 2. Bate el queso con azucar y huevos. "
                        + "3. Hornea a 160C 50 min. 4. Cubre con fresas.",
                90, Dificultad.MEDIA, 10, "651989", 520, postres,
                ing("Queso crema", "600", "g"), ing("Galletas", "200", "g"),
                ing("Mantequilla", "90", "g"), ing("Azucar", "150", "g"), ing("Fresas", "250", "g")));

        recetas.add(receta("Pasta carbonara", "Clasica pasta italiana con huevo y panceta",
                "1. Cocina la pasta. 2. Dora la panceta. 3. Mezcla con huevo y queso fuera del fuego. 4. Sirve caliente.",
                25, Dificultad.FACIL, 4, "636360", 598, principales,
                ing("Spaghetti", "400", "g"), ing("Panceta", "150", "g"),
                ing("Huevos", "4", "unidades"), ing("Queso parmesano", "80", "g")));

        recetas.add(receta("Pollo al curry", "Pollo cremoso con curry y leche de coco",
                "1. Sofrie cebolla y ajo. 2. Anade el pollo y dora. 3. Agrega curry y leche de coco. "
                        + "4. Cocina 20 min y sirve con arroz.",
                45, Dificultad.MEDIA, 4, "642499", 540, principales,
                ing("Pechuga de pollo", "500", "g"), ing("Leche de coco", "400", "ml"),
                ing("Curry en polvo", "2", "cucharadas"), ing("Cebolla", "1", "unidad")));

        recetas.add(receta("Tacos al pastor", "Tacos mexicanos de cerdo marinado con pina",
                "1. Marina el cerdo con achiote. 2. Asa la carne. 3. Calienta las tortillas. "
                        + "4. Sirve con pina, cebolla y cilantro.",
                60, Dificultad.DIFICIL, 6, "656723", 480, principales,
                ing("Carne de cerdo", "700", "g"), ing("Pasta de achiote", "60", "g"),
                ing("Pina", "200", "g"), ing("Tortillas de maiz", "12", "unidades")));

        recetas.add(receta("Limonada de menta", "Refrescante limonada con hierbabuena",
                "1. Exprime los limones. 2. Mezcla con agua y azucar. 3. Anade hojas de menta y hielo.",
                10, Dificultad.FACIL, 4, "661240", 90, bebidas,
                ing("Limones", "4", "unidades"), ing("Agua", "1", "litro"),
                ing("Azucar", "80", "g"), ing("Menta", "10", "hojas")));

        recetas.add(receta("Smoothie de frutos rojos", "Batido cremoso de fresa, mora y arandano",
                "1. Coloca las frutas en la licuadora. 2. Anade yogur y miel. 3. Licua hasta integrar.",
                8, Dificultad.FACIL, 2, "643241", 210, bebidas,
                ing("Frutos rojos", "250", "g"), ing("Yogur natural", "200", "g"), ing("Miel", "2", "cucharadas")));

        recetas.add(receta("Ensalada Cesar", "Lechuga, crutones, parmesano y aderezo cesar",
                "1. Corta la lechuga. 2. Prepara el aderezo. 3. Mezcla con crutones y parmesano.",
                15, Dificultad.FACIL, 2, "636682", 320, ensaladas,
                ing("Lechuga romana", "1", "unidad"), ing("Crutones", "80", "g"),
                ing("Queso parmesano", "50", "g"), ing("Aderezo cesar", "60", "ml")));

        recetas.add(receta("Ensalada caprese", "Tomate, mozzarella fresca y albahaca",
                "1. Corta tomate y mozzarella en rodajas. 2. Intercala con albahaca. 3. Rocia aceite de oliva.",
                10, Dificultad.FACIL, 2, "653819", 280, ensaladas,
                ing("Tomate", "3", "unidades"), ing("Mozzarella fresca", "200", "g"),
                ing("Albahaca", "1", "manojo"), ing("Aceite de oliva", "30", "ml")));

        recetas.add(receta("Pancakes esponjosos", "Tortitas americanas suaves para el desayuno",
                "1. Mezcla los secos. 2. Anade leche, huevo y mantequilla. 3. Cocina en sarten. 4. Sirve con miel.",
                20, Dificultad.FACIL, 4, "643153", 350, desayunos,
                ing("Harina", "200", "g"), ing("Leche", "250", "ml"),
                ing("Huevo", "1", "unidad"), ing("Polvo de hornear", "1", "cucharada")));

        recetaRepository.saveAll(recetas);
        log.info("Datos demo sembrados: {} recetas", recetas.size());
    }

    private Categoria cat(String nombre, String descripcion) {
        return categoriaRepository.save(Categoria.builder().nombre(nombre).descripcion(descripcion).build());
    }

    private Receta receta(String nombre, String desc, String instrucciones, int tiempo, Dificultad dif,
                          int porciones, String imgId, int calorias, Categoria categoria, Ingrediente... ings) {
        Receta r = Receta.builder()
                .nombre(nombre).descripcion(desc).instrucciones(instrucciones)
                .tiempoPreparacion(tiempo).dificultad(dif).porciones(porciones)
                .imagenUrl("https://img.spoonacular.com/recipes/" + imgId + "-636x393.jpg")
                .calorias(calorias).categoria(categoria).build();
        for (Ingrediente i : ings) {
            i.setReceta(r);
            r.getIngredientes().add(i);
        }
        return r;
    }

    private Ingrediente ing(String nombre, String cantidad, String unidad) {
        return Ingrediente.builder().nombre(nombre).cantidad(cantidad).unidad(unidad).build();
    }
}
