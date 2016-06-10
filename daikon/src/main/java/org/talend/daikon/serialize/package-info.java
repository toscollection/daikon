/**
 * This package supports serialization and deserialization of Java objects using a JSON string for both communication and
 * persistent purposes. It supports migration of persistent data from older versions.
 * 
 * <h2>Requirements</h2>
 * <ol>
 * <li>Persistent metadata saved in an older version must be completely usable in any future version.
 * <li>If older metadata is detected, the user can optionally be notified that a migration took place so the data can be resaved
 * in the current version.
 * <li>If possible, metadata in a newer version should work correctly in older versions. But this may not always be possible.
 * </ol>
 * 
 * <h2>Assumptions</h2>
 * <p>
 * The serialization is at the Java class level; each class manages its properties/fields and manages the migration issues related
 * to changes. The class is completely self-contained to allow for reuse. There is no separate mechanism (outside of the class)
 * that handles migration.
 * 
 * <p>
 * The persistent format is JSON as generated by the https://github.com/jdereg/json-io code. This was selected because it's able
 * to transparently and automatically serialize and deserialize Java objects comprised of true object graphs (with cycles) without
 * any special annotations. As of this writing, this is the only JSON package that does this. If this sort of feature gets added
 * to Jackson (for example), then we can consider migrating to that.
 * 
 * <h2>Possible Changes</h2> The possible changes to the class are:
 * 
 * <ol>
 * <li>Add a field/property.
 * <li>Remove a field/property.
 * <li>Change the semantic meaning of a field/property - which could result in the change of the value during a migration. Should
 * be rare, but possible. One example of this is the the property value was set incorrectly due to a bug and is set correctly (and
 * assumed to be correct) in a future version. The value from the older versions needs to be fixed.
 * <li>Change the datatype of a field/property - rare and similar to the above.
 * <li>Take any other action upon deserialization of a known older version by having a version number identify that particular
 * version.
 * </ol>
 * 
 * <h2>Implementation</h2>
 * <p>
 * Here are the rules and techniques for handling the possible changes:
 * 
 * <ol>
 * <li>Add - No special handling is required. Older software versions will be able to read newer objects without problems. The
 * deserialization mechanism will make sure that unknown fields are ignored.
 * <li>Remove - The {@link org.talend.daikon.serialize.DeserializeDeletedFieldHandler} interface is implemented by the class to
 * provide a method to handle the value associated with the deleted field. This method is called automatically by the JSON
 * deserializer.
 * <li>Other Changes - A means of providing a version number associated with the serialized object is provided using the
 * {@link org.talend.daikon.serialize.SerializeSetVersion} interface. This can be used for example if the meaning of a field, and
 * therefore its content, has been changed. When the change is made, a higher version number is provided with the changed
 * implementation. The code for handling doing the actual changes is provided using the
 * {@link org.talend.daikon.serialize.PostDeserializeHandler} which is called after the object has been completely materialized,
 * and the version number of the serialized object is provided.
 * </ol>
 */
package org.talend.daikon.serialize;