package com.example.model

import com.example.model.SlotShapeSpec.TriangleType
import kotlin.math.cos
import kotlin.math.sin

object FrameTemplates {

    private val allTemplates: List<FrameTemplate> = listOf(
        // ==========================================
        // 2 PHOTOS
        // ==========================================
        // Rectangular 2
        FrameTemplate(
            id = "rect_2_vert",
            name = "Vertical Split",
            category = FrameCategory.RECTANGULAR,
            photoCount = 2,
            slots = listOf(
                SlotSpec(0, 0f, 0f, 0.5f, 1f),
                SlotSpec(1, 0.5f, 0f, 1f, 1f)
            )
        ),
        FrameTemplate(
            id = "rect_2_horiz",
            name = "Horizontal Split",
            category = FrameCategory.RECTANGULAR,
            photoCount = 2,
            slots = listOf(
                SlotSpec(0, 0f, 0f, 1f, 0.5f),
                SlotSpec(1, 0f, 0.5f, 1f, 1f)
            )
        ),
        // Triangle 2
        FrameTemplate(
            id = "tri_2_diag",
            name = "Diagonal Triangles",
            category = FrameCategory.TRIANGLE,
            photoCount = 2,
            slots = listOf(
                SlotSpec(0, 0f, 0f, 1f, 1f, SlotShapeSpec.Triangle(TriangleType.DIAGONAL_TOP_LEFT)),
                SlotSpec(1, 0f, 0f, 1f, 1f, SlotShapeSpec.Triangle(TriangleType.DIAGONAL_BOTTOM_RIGHT))
            )
        ),
        // Quad 2
        FrameTemplate(
            id = "quad_2_slant",
            name = "Slanted Slash Quad",
            category = FrameCategory.QUAD,
            photoCount = 2,
            slots = listOf(
                SlotSpec(
                    0, 0f, 0f, 0.65f, 1f,
                    SlotShapeSpec.Polygon(listOf(Pair(0f, 0f), Pair(1f, 0f), Pair(0.6f, 1f), Pair(0f, 1f)))
                ),
                SlotSpec(
                    1, 0.35f, 0f, 1f, 1f,
                    SlotShapeSpec.Polygon(listOf(Pair(0.4f, 0f), Pair(1f, 0f), Pair(1f, 1f), Pair(0f, 1f)))
                )
            )
        ),
        // Heart 2
        FrameTemplate(
            id = "heart_2_duo",
            name = "Twin Hearts",
            category = FrameCategory.HEART,
            photoCount = 2,
            slots = listOf(
                SlotSpec(0, 0.04f, 0.22f, 0.52f, 0.78f, SlotShapeSpec.Heart),
                SlotSpec(1, 0.48f, 0.22f, 0.96f, 0.78f, SlotShapeSpec.Heart)
            )
        ),
        // Circle 2
        FrameTemplate(
            id = "circle_2_duo",
            name = "Dual Orbit Circles",
            category = FrameCategory.CIRCLE,
            photoCount = 2,
            slots = listOf(
                SlotSpec(0, 0.05f, 0.25f, 0.50f, 0.75f, SlotShapeSpec.Circle),
                SlotSpec(1, 0.50f, 0.25f, 0.95f, 0.75f, SlotShapeSpec.Circle)
            )
        ),

        // ==========================================
        // 3 PHOTOS
        // ==========================================
        // Rectangular 3
        FrameTemplate(
            id = "rect_3_hero_top",
            name = "Hero Top Split",
            category = FrameCategory.RECTANGULAR,
            photoCount = 3,
            slots = listOf(
                SlotSpec(0, 0f, 0f, 1f, 0.55f),
                SlotSpec(1, 0f, 0.55f, 0.5f, 1f),
                SlotSpec(2, 0.5f, 0.55f, 1f, 1f)
            )
        ),
        FrameTemplate(
            id = "rect_3_hero_left",
            name = "Hero Left Stack",
            category = FrameCategory.RECTANGULAR,
            photoCount = 3,
            slots = listOf(
                SlotSpec(0, 0f, 0f, 0.55f, 1f),
                SlotSpec(1, 0.55f, 0f, 1f, 0.5f),
                SlotSpec(2, 0.55f, 0.5f, 1f, 1f)
            )
        ),
        FrameTemplate(
            id = "rect_3_columns",
            name = "3 Columns",
            category = FrameCategory.RECTANGULAR,
            photoCount = 3,
            slots = listOf(
                SlotSpec(0, 0f, 0f, 0.333f, 1f),
                SlotSpec(1, 0.333f, 0f, 0.666f, 1f),
                SlotSpec(2, 0.666f, 0f, 1f, 1f)
            )
        ),
        // Triangle 3
        FrameTemplate(
            id = "tri_3_prism",
            name = "Triangular Prism",
            category = FrameCategory.TRIANGLE,
            photoCount = 3,
            slots = listOf(
                SlotSpec(0, 0f, 0f, 1f, 0.55f, SlotShapeSpec.Triangle(TriangleType.UP)),
                SlotSpec(0, 0f, 0.5f, 0.5f, 1f, SlotShapeSpec.Triangle(TriangleType.DOWN)),
                SlotSpec(1, 0.5f, 0.5f, 1f, 1f, SlotShapeSpec.Triangle(TriangleType.DOWN))
            ).mapIndexed { i, s -> s.copy(id = i) }
        ),
        // Quad 3
        FrameTemplate(
            id = "quad_3_triad",
            name = "Dynamic Quad Triad",
            category = FrameCategory.QUAD,
            photoCount = 3,
            slots = listOf(
                SlotSpec(
                    0, 0f, 0f, 0.55f, 1f,
                    SlotShapeSpec.Polygon(listOf(Pair(0f, 0f), Pair(1f, 0f), Pair(0.7f, 1f), Pair(0f, 1f)))
                ),
                SlotSpec(
                    1, 0.45f, 0f, 1f, 0.52f,
                    SlotShapeSpec.Polygon(listOf(Pair(0.2f, 0f), Pair(1f, 0f), Pair(1f, 1f), Pair(0f, 1f)))
                ),
                SlotSpec(
                    2, 0.38f, 0.48f, 1f, 1f,
                    SlotShapeSpec.Polygon(listOf(Pair(0.15f, 0f), Pair(1f, 0f), Pair(1f, 1f), Pair(0f, 1f)))
                )
            )
        ),
        // Heart 3
        FrameTemplate(
            id = "heart_3_trio",
            name = "Romantic Trio Hearts",
            category = FrameCategory.HEART,
            photoCount = 3,
            slots = listOf(
                SlotSpec(0, 0.15f, 0.04f, 0.85f, 0.58f, SlotShapeSpec.Heart),
                SlotSpec(1, 0.05f, 0.55f, 0.50f, 0.96f, SlotShapeSpec.Heart),
                SlotSpec(2, 0.50f, 0.55f, 0.95f, 0.96f, SlotShapeSpec.Heart)
            )
        ),
        // Circle 3
        FrameTemplate(
            id = "circle_3_cluster",
            name = "Tri-Circle Cluster",
            category = FrameCategory.CIRCLE,
            photoCount = 3,
            slots = listOf(
                SlotSpec(0, 0.22f, 0.04f, 0.78f, 0.54f, SlotShapeSpec.Circle),
                SlotSpec(1, 0.06f, 0.52f, 0.50f, 0.96f, SlotShapeSpec.Circle),
                SlotSpec(2, 0.50f, 0.52f, 0.94f, 0.96f, SlotShapeSpec.Circle)
            )
        ),

        // ==========================================
        // 4 PHOTOS
        // ==========================================
        // Rectangular 4
        FrameTemplate(
            id = "rect_4_grid",
            name = "2x2 Classic Grid",
            category = FrameCategory.RECTANGULAR,
            photoCount = 4,
            slots = listOf(
                SlotSpec(0, 0f, 0f, 0.5f, 0.5f),
                SlotSpec(1, 0.5f, 0f, 1f, 0.5f),
                SlotSpec(2, 0f, 0.5f, 0.5f, 1f),
                SlotSpec(3, 0.5f, 0.5f, 1f, 1f)
            )
        ),
        FrameTemplate(
            id = "rect_4_hero",
            name = "1 Hero + 3 Strips",
            category = FrameCategory.RECTANGULAR,
            photoCount = 4,
            slots = listOf(
                SlotSpec(0, 0f, 0f, 0.65f, 1f),
                SlotSpec(1, 0.65f, 0f, 1f, 0.333f),
                SlotSpec(2, 0.65f, 0.333f, 1f, 0.666f),
                SlotSpec(3, 0.65f, 0.666f, 1f, 1f)
            )
        ),
        // Triangle 4
        FrameTemplate(
            id = "tri_4_quadrants",
            name = "X-Cut Triangles",
            category = FrameCategory.TRIANGLE,
            photoCount = 4,
            slots = listOf(
                SlotSpec(0, 0f, 0f, 1f, 0.5f, SlotShapeSpec.Polygon(listOf(Pair(0f, 0f), Pair(1f, 0f), Pair(0.5f, 1f)))),
                SlotSpec(1, 0.5f, 0f, 1f, 1f, SlotShapeSpec.Polygon(listOf(Pair(1f, 0f), Pair(1f, 1f), Pair(0f, 0.5f)))),
                SlotSpec(2, 0f, 0.5f, 1f, 1f, SlotShapeSpec.Polygon(listOf(Pair(0.5f, 0f), Pair(1f, 1f), Pair(0f, 1f)))),
                SlotSpec(3, 0f, 0f, 0.5f, 1f, SlotShapeSpec.Polygon(listOf(Pair(0f, 0f), Pair(1f, 0.5f), Pair(0f, 1f))))
            )
        ),
        // Quad 4
        FrameTemplate(
            id = "quad_4_dynamic",
            name = "Asymmetric Quad Tiles",
            category = FrameCategory.QUAD,
            photoCount = 4,
            slots = listOf(
                SlotSpec(
                    0, 0f, 0f, 0.55f, 0.55f,
                    SlotShapeSpec.Polygon(listOf(Pair(0f, 0f), Pair(1f, 0f), Pair(0.85f, 1f), Pair(0f, 0.85f)))
                ),
                SlotSpec(
                    1, 0.45f, 0f, 1f, 0.55f,
                    SlotShapeSpec.Polygon(listOf(Pair(0.15f, 0f), Pair(1f, 0f), Pair(1f, 0.88f), Pair(0f, 1f)))
                ),
                SlotSpec(
                    2, 0f, 0.45f, 0.55f, 1f,
                    SlotShapeSpec.Polygon(listOf(Pair(0f, 0.12f), Pair(0.85f, 0f), Pair(1f, 1f), Pair(0f, 1f)))
                ),
                SlotSpec(
                    3, 0.45f, 0.45f, 1f, 1f,
                    SlotShapeSpec.Polygon(listOf(Pair(0f, 0.12f), Pair(1f, 0f), Pair(1f, 1f), Pair(0.15f, 1f)))
                )
            )
        ),
        // Heart 4
        FrameTemplate(
            id = "heart_4_quad",
            name = "Heart 4-Square",
            category = FrameCategory.HEART,
            photoCount = 4,
            slots = listOf(
                SlotSpec(0, 0.05f, 0.05f, 0.48f, 0.48f, SlotShapeSpec.Heart),
                SlotSpec(1, 0.52f, 0.05f, 0.95f, 0.48f, SlotShapeSpec.Heart),
                SlotSpec(2, 0.05f, 0.52f, 0.48f, 0.95f, SlotShapeSpec.Heart),
                SlotSpec(3, 0.52f, 0.52f, 0.95f, 0.95f, SlotShapeSpec.Heart)
            )
        ),
        // Circle 4
        FrameTemplate(
            id = "circle_4_bubbles",
            name = "4-Bubble Grid",
            category = FrameCategory.CIRCLE,
            photoCount = 4,
            slots = listOf(
                SlotSpec(0, 0.06f, 0.06f, 0.48f, 0.48f, SlotShapeSpec.Circle),
                SlotSpec(1, 0.52f, 0.06f, 0.94f, 0.48f, SlotShapeSpec.Circle),
                SlotSpec(2, 0.06f, 0.52f, 0.48f, 0.94f, SlotShapeSpec.Circle),
                SlotSpec(3, 0.52f, 0.52f, 0.94f, 0.94f, SlotShapeSpec.Circle)
            )
        ),

        // ==========================================
        // 5 PHOTOS
        // ==========================================
        // Rectangular 5
        FrameTemplate(
            id = "rect_5_split",
            name = "2 Top + 3 Bottom",
            category = FrameCategory.RECTANGULAR,
            photoCount = 5,
            slots = listOf(
                SlotSpec(0, 0f, 0f, 0.5f, 0.5f),
                SlotSpec(1, 0.5f, 0f, 1f, 0.5f),
                SlotSpec(2, 0f, 0.5f, 0.333f, 1f),
                SlotSpec(3, 0.333f, 0.5f, 0.666f, 1f),
                SlotSpec(4, 0.666f, 0.5f, 1f, 1f)
            )
        ),
        FrameTemplate(
            id = "rect_5_hero_center",
            name = "Center Hero + 4 Corners",
            category = FrameCategory.RECTANGULAR,
            photoCount = 5,
            slots = listOf(
                SlotSpec(0, 0.22f, 0.22f, 0.78f, 0.78f),
                SlotSpec(1, 0f, 0f, 0.5f, 0.22f),
                SlotSpec(2, 0.5f, 0f, 1f, 0.22f),
                SlotSpec(3, 0f, 0.78f, 0.5f, 1f),
                SlotSpec(4, 0.5f, 0.78f, 1f, 1f)
            )
        ),
        // Triangle 5
        FrameTemplate(
            id = "tri_5_star",
            name = "Delta 5 Geometric",
            category = FrameCategory.TRIANGLE,
            photoCount = 5,
            slots = listOf(
                SlotSpec(0, 0.25f, 0.25f, 0.75f, 0.75f, SlotShapeSpec.Triangle(TriangleType.UP)),
                SlotSpec(1, 0f, 0f, 0.5f, 0.35f, SlotShapeSpec.Triangle(TriangleType.DIAGONAL_TOP_LEFT)),
                SlotSpec(2, 0.5f, 0f, 1f, 0.35f, SlotShapeSpec.Triangle(TriangleType.DIAGONAL_TOP_RIGHT)),
                SlotSpec(3, 0f, 0.65f, 0.5f, 1f, SlotShapeSpec.Triangle(TriangleType.DIAGONAL_BOTTOM_LEFT)),
                SlotSpec(4, 0.5f, 0.65f, 1f, 1f, SlotShapeSpec.Triangle(TriangleType.DIAGONAL_BOTTOM_RIGHT))
            )
        ),
        // Quad 5
        FrameTemplate(
            id = "quad_5_mosaic",
            name = "5-Piece Quad Mosaic",
            category = FrameCategory.QUAD,
            photoCount = 5,
            slots = listOf(
                SlotSpec(
                    0, 0f, 0f, 0.6f, 0.45f,
                    SlotShapeSpec.Polygon(listOf(Pair(0f, 0f), Pair(1f, 0f), Pair(0.8f, 1f), Pair(0f, 1f)))
                ),
                SlotSpec(
                    1, 0.5f, 0f, 1f, 0.55f,
                    SlotShapeSpec.Polygon(listOf(Pair(0.2f, 0f), Pair(1f, 0f), Pair(1f, 1f), Pair(0f, 1f)))
                ),
                SlotSpec(
                    2, 0f, 0.45f, 0.4f, 1f,
                    SlotShapeSpec.Polygon(listOf(Pair(0f, 0f), Pair(1f, 0f), Pair(0.7f, 1f), Pair(0f, 1f)))
                ),
                SlotSpec(
                    3, 0.35f, 0.45f, 0.75f, 1f,
                    SlotShapeSpec.Polygon(listOf(Pair(0.1f, 0f), Pair(1f, 0.2f), Pair(0.8f, 1f), Pair(0f, 1f)))
                ),
                SlotSpec(
                    4, 0.65f, 0.55f, 1f, 1f,
                    SlotShapeSpec.Polygon(listOf(Pair(0.2f, 0f), Pair(1f, 0f), Pair(1f, 1f), Pair(0f, 1f)))
                )
            )
        ),
        // Heart 5
        FrameTemplate(
            id = "heart_5_center",
            name = "Hero Heart + 4 Satellite",
            category = FrameCategory.HEART,
            photoCount = 5,
            slots = listOf(
                SlotSpec(0, 0.22f, 0.22f, 0.78f, 0.78f, SlotShapeSpec.Heart),
                SlotSpec(1, 0.04f, 0.04f, 0.34f, 0.34f, SlotShapeSpec.Heart),
                SlotSpec(2, 0.66f, 0.04f, 0.96f, 0.34f, SlotShapeSpec.Heart),
                SlotSpec(3, 0.04f, 0.66f, 0.34f, 0.96f, SlotShapeSpec.Heart),
                SlotSpec(4, 0.66f, 0.66f, 0.96f, 0.96f, SlotShapeSpec.Heart)
            )
        ),
        // Circle 5
        FrameTemplate(
            id = "circle_5_orbit",
            name = "Hero Circle + 4 Orbits",
            category = FrameCategory.CIRCLE,
            photoCount = 5,
            slots = listOf(
                SlotSpec(0, 0.25f, 0.25f, 0.75f, 0.75f, SlotShapeSpec.Circle),
                SlotSpec(1, 0.05f, 0.05f, 0.35f, 0.35f, SlotShapeSpec.Circle),
                SlotSpec(2, 0.65f, 0.05f, 0.95f, 0.35f, SlotShapeSpec.Circle),
                SlotSpec(3, 0.05f, 0.65f, 0.35f, 0.95f, SlotShapeSpec.Circle),
                SlotSpec(4, 0.65f, 0.65f, 0.95f, 0.95f, SlotShapeSpec.Circle)
            )
        ),

        // ==========================================
        // 6 PHOTOS
        // ==========================================
        // Rectangular 6
        FrameTemplate(
            id = "rect_6_grid",
            name = "3x2 Grid",
            category = FrameCategory.RECTANGULAR,
            photoCount = 6,
            slots = listOf(
                SlotSpec(0, 0f, 0f, 0.333f, 0.5f),
                SlotSpec(1, 0.333f, 0f, 0.666f, 0.5f),
                SlotSpec(2, 0.666f, 0f, 1f, 0.5f),
                SlotSpec(3, 0f, 0.5f, 0.333f, 1f),
                SlotSpec(4, 0.333f, 0.5f, 0.666f, 1f),
                SlotSpec(5, 0.666f, 0.5f, 1f, 1f)
            )
        ),
        FrameTemplate(
            id = "rect_6_hero_mosaic",
            name = "Hero + 5 Surround",
            category = FrameCategory.RECTANGULAR,
            photoCount = 6,
            slots = listOf(
                SlotSpec(0, 0f, 0f, 0.666f, 0.666f),
                SlotSpec(1, 0.666f, 0f, 1f, 0.333f),
                SlotSpec(2, 0.666f, 0.333f, 1f, 0.666f),
                SlotSpec(3, 0f, 0.666f, 0.333f, 1f),
                SlotSpec(4, 0.333f, 0.666f, 0.666f, 1f),
                SlotSpec(5, 0.666f, 0.666f, 1f, 1f)
            )
        ),
        // Triangle 6
        FrameTemplate(
            id = "tri_6_kaleidoscope",
            name = "Kaleidoscope 6",
            category = FrameCategory.TRIANGLE,
            photoCount = 6,
            slots = run {
                val slots = mutableListOf<SlotSpec>()
                for (i in 0 until 6) {
                    val angle1 = Math.toRadians((i * 60.0))
                    val angle2 = Math.toRadians(((i + 1) * 60.0))
                    val p1 = Pair(0.5f + (0.5f * cos(angle1)).toFloat(), 0.5f + (0.5f * sin(angle1)).toFloat())
                    val p2 = Pair(0.5f + (0.5f * cos(angle2)).toFloat(), 0.5f + (0.5f * sin(angle2)).toFloat())
                    slots.add(
                        SlotSpec(
                            i, 0f, 0f, 1f, 1f,
                            SlotShapeSpec.Polygon(listOf(Pair(0.5f, 0.5f), p1, p2))
                        )
                    )
                }
                slots
            }
        ),
        // Quad 6
        FrameTemplate(
            id = "quad_6_faceted",
            name = "Faceted 6-Quad",
            category = FrameCategory.QUAD,
            photoCount = 6,
            slots = listOf(
                SlotSpec(0, 0f, 0f, 0.55f, 0.35f, SlotShapeSpec.Polygon(listOf(Pair(0f, 0f), Pair(1f, 0f), Pair(0.85f, 1f), Pair(0f, 1f)))),
                SlotSpec(1, 0.45f, 0f, 1f, 0.35f, SlotShapeSpec.Polygon(listOf(Pair(0.15f, 0f), Pair(1f, 0f), Pair(1f, 1f), Pair(0f, 1f)))),
                SlotSpec(2, 0f, 0.35f, 0.45f, 0.70f, SlotShapeSpec.Polygon(listOf(Pair(0f, 0f), Pair(1f, 0f), Pair(0.85f, 1f), Pair(0f, 1f)))),
                SlotSpec(3, 0.45f, 0.35f, 1f, 0.70f, SlotShapeSpec.Polygon(listOf(Pair(0f, 0f), Pair(1f, 0f), Pair(1f, 1f), Pair(0.15f, 1f)))),
                SlotSpec(4, 0f, 0.70f, 0.55f, 1f, SlotShapeSpec.Polygon(listOf(Pair(0f, 0f), Pair(0.85f, 0f), Pair(1f, 1f), Pair(0f, 1f)))),
                SlotSpec(5, 0.45f, 0.70f, 1f, 1f, SlotShapeSpec.Polygon(listOf(Pair(0.15f, 0f), Pair(1f, 0f), Pair(1f, 1f), Pair(0f, 1f))))
            )
        ),
        // Heart 6
        FrameTemplate(
            id = "heart_6_ring",
            name = "Heart Constellation 6",
            category = FrameCategory.HEART,
            photoCount = 6,
            slots = listOf(
                SlotSpec(0, 0.16f, 0.08f, 0.48f, 0.40f, SlotShapeSpec.Heart),
                SlotSpec(1, 0.52f, 0.08f, 0.84f, 0.40f, SlotShapeSpec.Heart),
                SlotSpec(2, 0.04f, 0.38f, 0.36f, 0.70f, SlotShapeSpec.Heart),
                SlotSpec(3, 0.64f, 0.38f, 0.96f, 0.70f, SlotShapeSpec.Heart),
                SlotSpec(4, 0.22f, 0.62f, 0.52f, 0.94f, SlotShapeSpec.Heart),
                SlotSpec(5, 0.48f, 0.62f, 0.78f, 0.94f, SlotShapeSpec.Heart)
            )
        ),
        // Circle 6
        FrameTemplate(
            id = "circle_6_flower",
            name = "6-Circle Ring",
            category = FrameCategory.CIRCLE,
            photoCount = 6,
            slots = run {
                val slots = mutableListOf<SlotSpec>()
                val r = 0.30f
                val size = 0.34f
                for (i in 0 until 6) {
                    val angle = Math.toRadians(i * 60.0 - 90.0)
                    val cx = (0.5 + r * cos(angle)).toFloat()
                    val cy = (0.5 + r * sin(angle)).toFloat()
                    slots.add(
                        SlotSpec(i, cx - size / 2f, cy - size / 2f, cx + size / 2f, cy + size / 2f, SlotShapeSpec.Circle)
                    )
                }
                slots
            }
        ),

        // ==========================================
        // 7 PHOTOS
        // ==========================================
        // Rectangular 7
        FrameTemplate(
            id = "rect_7_strip",
            name = "3 Top + 1 Wide + 3 Bottom",
            category = FrameCategory.RECTANGULAR,
            photoCount = 7,
            slots = listOf(
                SlotSpec(0, 0f, 0f, 0.333f, 0.35f),
                SlotSpec(1, 0.333f, 0f, 0.666f, 0.35f),
                SlotSpec(2, 0.666f, 0f, 1f, 0.35f),
                SlotSpec(3, 0f, 0.35f, 1f, 0.65f),
                SlotSpec(4, 0f, 0.65f, 0.333f, 1f),
                SlotSpec(5, 0.333f, 0.65f, 0.666f, 1f),
                SlotSpec(6, 0.666f, 0.65f, 1f, 1f)
            )
        ),
        // Triangle 7
        FrameTemplate(
            id = "tri_7_lattice",
            name = "7-Triangle Lattice",
            category = FrameCategory.TRIANGLE,
            photoCount = 7,
            slots = listOf(
                SlotSpec(0, 0.35f, 0.35f, 0.65f, 0.65f, SlotShapeSpec.Triangle(TriangleType.UP)),
                SlotSpec(1, 0.1f, 0.05f, 0.5f, 0.40f, SlotShapeSpec.Triangle(TriangleType.UP)),
                SlotSpec(2, 0.5f, 0.05f, 0.9f, 0.40f, SlotShapeSpec.Triangle(TriangleType.DOWN)),
                SlotSpec(3, 0.02f, 0.35f, 0.35f, 0.70f, SlotShapeSpec.Triangle(TriangleType.LEFT)),
                SlotSpec(4, 0.65f, 0.35f, 0.98f, 0.70f, SlotShapeSpec.Triangle(TriangleType.RIGHT)),
                SlotSpec(5, 0.1f, 0.65f, 0.5f, 0.98f, SlotShapeSpec.Triangle(TriangleType.DOWN)),
                SlotSpec(6, 0.5f, 0.65f, 0.9f, 0.98f, SlotShapeSpec.Triangle(TriangleType.UP))
            )
        ),
        // Quad 7
        FrameTemplate(
            id = "quad_7_masonry",
            name = "7-Quad Modern Masonry",
            category = FrameCategory.QUAD,
            photoCount = 7,
            slots = listOf(
                SlotSpec(0, 0f, 0f, 0.4f, 0.35f),
                SlotSpec(1, 0.4f, 0f, 0.7f, 0.35f),
                SlotSpec(2, 0.7f, 0f, 1f, 0.35f),
                SlotSpec(3, 0f, 0.35f, 0.55f, 0.65f),
                SlotSpec(4, 0.55f, 0.35f, 1f, 0.65f),
                SlotSpec(5, 0f, 0.65f, 0.5f, 1f),
                SlotSpec(6, 0.5f, 0.65f, 1f, 1f)
            )
        ),
        // Heart 7
        FrameTemplate(
            id = "heart_7_ensemble",
            name = "7-Heart Silhouette",
            category = FrameCategory.HEART,
            photoCount = 7,
            slots = listOf(
                SlotSpec(0, 0.34f, 0.34f, 0.66f, 0.66f, SlotShapeSpec.Heart),
                SlotSpec(1, 0.16f, 0.06f, 0.44f, 0.36f, SlotShapeSpec.Heart),
                SlotSpec(2, 0.56f, 0.06f, 0.84f, 0.36f, SlotShapeSpec.Heart),
                SlotSpec(3, 0.04f, 0.34f, 0.32f, 0.64f, SlotShapeSpec.Heart),
                SlotSpec(4, 0.68f, 0.34f, 0.96f, 0.64f, SlotShapeSpec.Heart),
                SlotSpec(5, 0.20f, 0.66f, 0.48f, 0.96f, SlotShapeSpec.Heart),
                SlotSpec(6, 0.52f, 0.66f, 0.80f, 0.96f, SlotShapeSpec.Heart)
            )
        ),
        // Circle 7
        FrameTemplate(
            id = "circle_7_planetary",
            name = "Hero Sun + 6 Planets",
            category = FrameCategory.CIRCLE,
            photoCount = 7,
            slots = run {
                val slots = mutableListOf<SlotSpec>()
                // Center sun
                slots.add(SlotSpec(0, 0.30f, 0.30f, 0.70f, 0.70f, SlotShapeSpec.Circle))
                // 6 satellites
                val r = 0.36f
                val size = 0.25f
                for (i in 0 until 6) {
                    val angle = Math.toRadians(i * 60.0 - 90.0)
                    val cx = (0.5 + r * cos(angle)).toFloat()
                    val cy = (0.5 + r * sin(angle)).toFloat()
                    slots.add(
                        SlotSpec(i + 1, cx - size / 2f, cy - size / 2f, cx + size / 2f, cy + size / 2f, SlotShapeSpec.Circle)
                    )
                }
                slots
            }
        ),

        // ==========================================
        // 8 PHOTOS
        // ==========================================
        // Rectangular 8
        FrameTemplate(
            id = "rect_8_grid",
            name = "4x2 Grid",
            category = FrameCategory.RECTANGULAR,
            photoCount = 8,
            slots = listOf(
                SlotSpec(0, 0f, 0f, 0.25f, 0.5f),
                SlotSpec(1, 0.25f, 0f, 0.50f, 0.5f),
                SlotSpec(2, 0.50f, 0f, 0.75f, 0.5f),
                SlotSpec(3, 0.75f, 0f, 1f, 0.5f),
                SlotSpec(4, 0f, 0.5f, 0.25f, 1f),
                SlotSpec(5, 0.25f, 0.5f, 0.50f, 1f),
                SlotSpec(6, 0.50f, 0.5f, 0.75f, 1f),
                SlotSpec(7, 0.75f, 0.5f, 1f, 1f)
            )
        ),
        FrameTemplate(
            id = "rect_8_mosaic",
            name = "2 Center + 6 Surround",
            category = FrameCategory.RECTANGULAR,
            photoCount = 8,
            slots = listOf(
                SlotSpec(0, 0f, 0f, 0.333f, 0.333f),
                SlotSpec(1, 0.333f, 0f, 0.666f, 0.333f),
                SlotSpec(2, 0.666f, 0f, 1f, 0.333f),
                SlotSpec(3, 0f, 0.333f, 0.333f, 0.666f),
                SlotSpec(4, 0.666f, 0.333f, 1f, 0.666f),
                SlotSpec(5, 0f, 0.666f, 0.333f, 1f),
                SlotSpec(6, 0.333f, 0.666f, 0.666f, 1f),
                SlotSpec(7, 0.666f, 0.666f, 1f, 1f)
            )
        ),
        // Triangle 8
        FrameTemplate(
            id = "tri_8_octa",
            name = "Octa-Triangle Wheel",
            category = FrameCategory.TRIANGLE,
            photoCount = 8,
            slots = run {
                val slots = mutableListOf<SlotSpec>()
                for (i in 0 until 8) {
                    val a1 = Math.toRadians(i * 45.0)
                    val a2 = Math.toRadians((i + 1) * 45.0)
                    val p1 = Pair(0.5f + (0.5f * cos(a1)).toFloat(), 0.5f + (0.5f * sin(a1)).toFloat())
                    val p2 = Pair(0.5f + (0.5f * cos(a2)).toFloat(), 0.5f + (0.5f * sin(a2)).toFloat())
                    slots.add(
                        SlotSpec(i, 0f, 0f, 1f, 1f, SlotShapeSpec.Polygon(listOf(Pair(0.5f, 0.5f), p1, p2)))
                    )
                }
                slots
            }
        ),
        // Quad 8
        FrameTemplate(
            id = "quad_8_mosaic",
            name = "8-Tile Quad Mosaic",
            category = FrameCategory.QUAD,
            photoCount = 8,
            slots = listOf(
                SlotSpec(0, 0f, 0f, 0.333f, 0.35f),
                SlotSpec(1, 0.333f, 0f, 0.666f, 0.35f),
                SlotSpec(2, 0.666f, 0f, 1f, 0.35f),
                SlotSpec(3, 0f, 0.35f, 0.5f, 0.65f),
                SlotSpec(4, 0.5f, 0.35f, 1f, 0.65f),
                SlotSpec(5, 0f, 0.65f, 0.333f, 1f),
                SlotSpec(6, 0.333f, 0.65f, 0.666f, 1f),
                SlotSpec(7, 0.666f, 0.65f, 1f, 1f)
            )
        ),
        // Heart 8
        FrameTemplate(
            id = "heart_8_contour",
            name = "Grand Heart Collage 8",
            category = FrameCategory.HEART,
            photoCount = 8,
            slots = listOf(
                SlotSpec(0, 0.18f, 0.04f, 0.44f, 0.32f, SlotShapeSpec.Heart),
                SlotSpec(1, 0.56f, 0.04f, 0.82f, 0.32f, SlotShapeSpec.Heart),
                SlotSpec(2, 0.04f, 0.28f, 0.30f, 0.56f, SlotShapeSpec.Heart),
                SlotSpec(3, 0.70f, 0.28f, 0.96f, 0.56f, SlotShapeSpec.Heart),
                SlotSpec(4, 0.10f, 0.54f, 0.36f, 0.82f, SlotShapeSpec.Heart),
                SlotSpec(5, 0.64f, 0.54f, 0.90f, 0.82f, SlotShapeSpec.Heart),
                SlotSpec(6, 0.37f, 0.68f, 0.63f, 0.96f, SlotShapeSpec.Heart),
                SlotSpec(7, 0.36f, 0.32f, 0.64f, 0.60f, SlotShapeSpec.Heart)
            )
        ),
        // Circle 8
        FrameTemplate(
            id = "circle_8_ring",
            name = "8-Circle Orbit",
            category = FrameCategory.CIRCLE,
            photoCount = 8,
            slots = run {
                val slots = mutableListOf<SlotSpec>()
                val r = 0.35f
                val size = 0.26f
                for (i in 0 until 8) {
                    val angle = Math.toRadians(i * 45.0 - 90.0)
                    val cx = (0.5 + r * cos(angle)).toFloat()
                    val cy = (0.5 + r * sin(angle)).toFloat()
                    slots.add(
                        SlotSpec(i, cx - size / 2f, cy - size / 2f, cx + size / 2f, cy + size / 2f, SlotShapeSpec.Circle)
                    )
                }
                slots
            }
        )
    )

    fun getTemplates(photoCount: Int, category: FrameCategory? = null): List<FrameTemplate> {
        return allTemplates.filter {
            it.photoCount == photoCount && (category == null || it.category == category)
        }
    }

    fun getDefaultTemplate(photoCount: Int, category: FrameCategory = FrameCategory.RECTANGULAR): FrameTemplate {
        return getTemplates(photoCount, category).firstOrNull()
            ?: allTemplates.first { it.photoCount == photoCount }
    }
}
