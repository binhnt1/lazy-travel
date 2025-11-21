package com.lazytravel.data.remote.schema

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * Schema Definition System for PocketBase Collections
 *
 * Provides a Kotlin DSL to define collection schemas in a type-safe, readable way.
 *
 * Example:
 * ```
 * val mySchema = collectionSchema {
 *     name = "destinations"
 *     type = CollectionType.BASE
 *
 *     fields {
 *         text("name") {
 *             required = true
 *             min = 2
 *             max = 200
 *         }
 *
 *         number("rating") {
 *             min = 0.0
 *             max = 5.0
 *         }
 *     }
 * }
 * ```
 */

/**
 * Collection Types
 */
enum class CollectionType(val value: String) {
    BASE("base"),           // Regular collection
    AUTH("auth"),           // User authentication collection
    VIEW("view")            // Read-only view
}

/**
 * Field Types
 */
enum class FieldType(val value: String) {
    TEXT("text"),
    NUMBER("number"),
    BOOL("bool"),
    EMAIL("email"),
    URL("url"),
    DATE("date"),
    SELECT("select"),
    JSON("json"),
    FILE("file"),
    RELATION("relation"),
    EDITOR("editor")       // Rich text editor
}

/**
 * Main Collection Schema
 */
@Serializable
data class CollectionSchema(
    var name: String = "",
    var type: CollectionType = CollectionType.BASE,
    val schema: MutableList<FieldSchema> = mutableListOf(),
    val indexes: MutableList<String> = mutableListOf(),
    var listRule: String? = null,      // API Rules
    var viewRule: String? = null,
    var createRule: String? = null,
    var updateRule: String? = null,
    var deleteRule: String? = null
)

/**
 * Field Schema
 */
@Serializable
data class FieldSchema(
    val name: String,
    val type: FieldType,
    var required: Boolean = false,
    val options: MutableMap<String, @Contextual Any> = mutableMapOf()
)

/**
 * DSL Builder for Collection Schema
 */
class CollectionSchemaBuilder {
    private val schema = CollectionSchema()

    var name: String
        get() = schema.name
        set(value) { schema.name = value }

    var type: CollectionType
        get() = schema.type
        set(value) { schema.type = value }

    // API Rules
    var listRule: String?
        get() = schema.listRule
        set(value) { schema.listRule = value }

    var viewRule: String?
        get() = schema.viewRule
        set(value) { schema.viewRule = value }

    var createRule: String?
        get() = schema.createRule
        set(value) { schema.createRule = value }

    var updateRule: String?
        get() = schema.updateRule
        set(value) { schema.updateRule = value }

    var deleteRule: String?
        get() = schema.deleteRule
        set(value) { schema.deleteRule = value }

    /**
     * Define fields
     */
    fun fields(block: FieldsBuilder.() -> Unit) {
        val builder = FieldsBuilder(schema.schema)
        builder.block()
    }

    /**
     * Define indexes
     */
    fun indexes(block: IndexesBuilder.() -> Unit) {
        val builder = IndexesBuilder(schema.indexes)
        builder.block()
    }

    fun build(): CollectionSchema = schema
}

/**
 * DSL Builder for Fields
 */
class FieldsBuilder(private val fields: MutableList<FieldSchema>) {

    /**
     * Text field
     */
    fun text(name: String, block: TextFieldBuilder.() -> Unit = {}) {
        val builder = TextFieldBuilder(name)
        builder.block()
        fields.add(builder.build())
    }

    /**
     * Number field
     */
    fun number(name: String, block: NumberFieldBuilder.() -> Unit = {}) {
        val builder = NumberFieldBuilder(name)
        builder.block()
        fields.add(builder.build())
    }

    /**
     * Boolean field
     */
    fun bool(name: String, block: BoolFieldBuilder.() -> Unit = {}) {
        val builder = BoolFieldBuilder(name)
        builder.block()
        fields.add(builder.build())
    }

    /**
     * Email field
     */
    fun email(name: String, block: EmailFieldBuilder.() -> Unit = {}) {
        val builder = EmailFieldBuilder(name)
        builder.block()
        fields.add(builder.build())
    }

    /**
     * URL field
     */
    fun url(name: String, block: UrlFieldBuilder.() -> Unit = {}) {
        val builder = UrlFieldBuilder(name)
        builder.block()
        fields.add(builder.build())
    }

    /**
     * Date field
     */
    fun date(name: String, block: DateFieldBuilder.() -> Unit = {}) {
        val builder = DateFieldBuilder(name)
        builder.block()
        fields.add(builder.build())
    }

    /**
     * Select field (dropdown/radio)
     */
    fun select(name: String, block: SelectFieldBuilder.() -> Unit = {}) {
        val builder = SelectFieldBuilder(name)
        builder.block()
        fields.add(builder.build())
    }

    /**
     * JSON field
     */
    fun json(name: String, block: JsonFieldBuilder.() -> Unit = {}) {
        val builder = JsonFieldBuilder(name)
        builder.block()
        fields.add(builder.build())
    }

    /**
     * File field (single/multiple files)
     */
    fun file(name: String, block: FileFieldBuilder.() -> Unit = {}) {
        val builder = FileFieldBuilder(name)
        builder.block()
        fields.add(builder.build())
    }

    /**
     * Relation field (foreign key)
     */
    fun relation(name: String, block: RelationFieldBuilder.() -> Unit = {}) {
        val builder = RelationFieldBuilder(name)
        builder.block()
        fields.add(builder.build())
    }

    /**
     * Editor field (rich text)
     */
    fun editor(name: String, block: EditorFieldBuilder.() -> Unit = {}) {
        val builder = EditorFieldBuilder(name)
        builder.block()
        fields.add(builder.build())
    }
}

/**
 * Base Field Builder
 */
abstract class BaseFieldBuilder(
    protected val name: String,
    protected val type: FieldType
) {
    protected val field = FieldSchema(name, type)

    var required: Boolean
        get() = this.field.required
        set(value) { this.field.required = value }

    fun build(): FieldSchema = field
}

/**
 * Text Field Builder
 */
class TextFieldBuilder(name: String) : BaseFieldBuilder(name, FieldType.TEXT) {
    var min: Int?
        get() = this.field.options["min"] as? Int
        set(newValue) {
            newValue?.let { this.field.options["min"] = it }
        }

    var max: Int?
        get() = this.field.options["max"] as? Int
        set(newValue) {
            newValue?.let { this.field.options["max"] = it }
        }

    var pattern: String?
        get() = this.field.options["pattern"] as? String
        set(newValue) {
            newValue?.let { this.field.options["pattern"] = it }
        }
}

/**
 * Number Field Builder
 */
class NumberFieldBuilder(name: String) : BaseFieldBuilder(name, FieldType.NUMBER) {
    var min: Double?
        get() = this.field.options["min"] as? Double
        set(newValue) {
            newValue?.let { this.field.options["min"] = it }
        }

    var max: Double?
        get() = this.field.options["max"] as? Double
        set(newValue) {
            newValue?.let { this.field.options["max"] = it }
        }

    var onlyInt: Boolean
        get() = (this.field.options["onlyInt"] as? Boolean) ?: false
        set(newValue) {
            this.field.options["onlyInt"] = newValue
        }
}

/**
 * Bool Field Builder
 */
class BoolFieldBuilder(name: String) : BaseFieldBuilder(name, FieldType.BOOL)

/**
 * Email Field Builder
 */
class EmailFieldBuilder(name: String) : BaseFieldBuilder(name, FieldType.EMAIL) {
    @Suppress("UNCHECKED_CAST")
    var exceptDomains: List<String>
        get() = (this.field.options["exceptDomains"] as? List<String>) ?: emptyList()
        set(value) {
            if (value.isNotEmpty()) {
                this.field.options["exceptDomains"] = value
            }
        }

    @Suppress("UNCHECKED_CAST")
    var onlyDomains: List<String>
        get() = (this.field.options["onlyDomains"] as? List<String>) ?: emptyList()
        set(value) {
            if (value.isNotEmpty()) {
                this.field.options["onlyDomains"] = value
            }
        }
}

/**
 * URL Field Builder
 */
class UrlFieldBuilder(name: String) : BaseFieldBuilder(name, FieldType.URL) {
    @Suppress("UNCHECKED_CAST")
    var exceptDomains: List<String>
        get() = (this.field.options["exceptDomains"] as? List<String>) ?: emptyList()
        set(value) {
            if (value.isNotEmpty()) {
                this.field.options["exceptDomains"] = value
            }
        }

    @Suppress("UNCHECKED_CAST")
    var onlyDomains: List<String>
        get() = (this.field.options["onlyDomains"] as? List<String>) ?: emptyList()
        set(value) {
            if (value.isNotEmpty()) {
                this.field.options["onlyDomains"] = value
            }
        }
}

/**
 * Date Field Builder
 */
class DateFieldBuilder(name: String) : BaseFieldBuilder(name, FieldType.DATE) {
    var min: String?
        get() = this.field.options["min"] as? String
        set(value) {
            value?.let { this.field.options["min"] = it }
        }

    var max: String?
        get() = this.field.options["max"] as? String
        set(value) {
            value?.let { this.field.options["max"] = it }
        }
}

/**
 * Select Field Builder
 */
class SelectFieldBuilder(name: String) : BaseFieldBuilder(name, FieldType.SELECT) {
    @Suppress("UNCHECKED_CAST")
    var values: List<String>
        get() = (this.field.options["values"] as? List<String>) ?: emptyList()
        set(newValue) {
            this.field.options["values"] = newValue
        }

    var maxSelect: Int
        get() = (this.field.options["maxSelect"] as? Int) ?: 1
        set(newValue) {
            this.field.options["maxSelect"] = newValue
        }
}

/**
 * JSON Field Builder
 */
class JsonFieldBuilder(name: String) : BaseFieldBuilder(name, FieldType.JSON) {
    var maxSize: Int?
        get() = this.field.options["maxSize"] as? Int
        set(value) {
            value?.let { this.field.options["maxSize"] = it }
        }
}

/**
 * File Field Builder
 */
class FileFieldBuilder(name: String) : BaseFieldBuilder(name, FieldType.FILE) {
    var maxSelect: Int
        get() = (this.field.options["maxSelect"] as? Int) ?: 1
        set(value) {
            this.field.options["maxSelect"] = value
        }

    var maxSize: Int
        get() = (this.field.options["maxSize"] as? Int) ?: 5242880  // 5MB default
        set(value) {
            this.field.options["maxSize"] = value
        }

    @Suppress("UNCHECKED_CAST")
    var mimeTypes: List<String>
        get() = (this.field.options["mimeTypes"] as? List<String>) ?: emptyList()
        set(value) {
            if (value.isNotEmpty()) {
                this.field.options["mimeTypes"] = value
            }
        }

    @Suppress("UNCHECKED_CAST")
    var thumbs: List<String>
        get() = (this.field.options["thumbs"] as? List<String>) ?: emptyList()
        set(value) {
            if (value.isNotEmpty()) {
                this.field.options["thumbs"] = value
            }
        }
}

/**
 * Relation Field Builder
 */
class RelationFieldBuilder(name: String) : BaseFieldBuilder(name, FieldType.RELATION) {

    init {
        // Set default maxSelect=1 to match PocketBase API requirements
        this.field.options["maxSelect"] = 1
    }

    var collectionId: String
        get() = (this.field.options["collectionId"] as? String) ?: ""
        set(newValue) {
            this.field.options["collectionId"] = newValue
        }

    var cascadeDelete: Boolean
        get() = (this.field.options["cascadeDelete"] as? Boolean) ?: false
        set(newValue) {
            this.field.options["cascadeDelete"] = newValue
        }

    var maxSelect: Int
        get() = (this.field.options["maxSelect"] as? Int) ?: 1
        set(newValue) {
            this.field.options["maxSelect"] = newValue
        }

    var minSelect: Int
        get() = (this.field.options["minSelect"] as? Int) ?: 0
        set(newValue) {
            this.field.options["minSelect"] = newValue
        }
}

/**
 * Editor Field Builder (Rich text)
 */
class EditorFieldBuilder(name: String) : BaseFieldBuilder(name, FieldType.EDITOR) {
    var convertUrls: Boolean
        get() = (this.field.options["convertUrls"] as? Boolean) ?: false
        set(value) {
            this.field.options["convertUrls"] = value
        }
}

/**
 * Indexes Builder
 */
class IndexesBuilder(private val indexes: MutableList<String>) {

    /**
     * Add a single field index
     */
    fun index(fieldName: String) {
        indexes.add("CREATE INDEX idx_${fieldName} ON {{collection}} (${fieldName})")
    }

    /**
     * Add a composite index (multiple fields)
     */
    fun compositeIndex(vararg fieldNames: String) {
        val fields = fieldNames.joinToString(", ")
        val name = fieldNames.joinToString("_")
        indexes.add("CREATE INDEX idx_${name} ON {{collection}} (${fields})")
    }

    /**
     * Add a unique index
     */
    fun uniqueIndex(fieldName: String) {
        indexes.add("CREATE UNIQUE INDEX idx_unique_${fieldName} ON {{collection}} (${fieldName})")
    }
}

/**
 * DSL entry point
 */
fun collectionSchema(block: CollectionSchemaBuilder.() -> Unit): CollectionSchema {
    val builder = CollectionSchemaBuilder()
    builder.block()
    return builder.build()
}
