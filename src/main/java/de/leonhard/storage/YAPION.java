package de.leonhard.storage;

import de.leonhard.storage.internal.FileData;
import de.leonhard.storage.internal.FileType;
import de.leonhard.storage.internal.FlatFile;
import de.leonhard.storage.internal.settings.ReloadSettings;
import de.leonhard.storage.util.FileUtils;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import yapion.exceptions.YAPIONException;
import yapion.hierarchy.output.FileOutput;
import yapion.hierarchy.types.YAPIONObject;
import yapion.parser.YAPIONParser;
import yapion.serializing.YAPIONDeserializer;
import yapion.serializing.YAPIONSerializer;

import java.io.*;
import java.util.Map;

public class YAPION extends FlatFile {

    public YAPION(@NonNull final YAPION yapion) {
        super(yapion.getFile());
        this.fileData = yapion.getFileData();
        this.pathPrefix = yapion.getPathPrefix();
    }

    protected YAPION(@NonNull String name, @Nullable String path, @NonNull FileType fileType) {
        super(name, path, fileType);
    }

    protected YAPION(@NonNull File file, @NonNull FileType fileType) {
        super(file, fileType);
    }

    protected YAPION(@NonNull File file) {
        super(file);
    }

    public YAPION(
            final String name,
            final String path,
            final InputStream inputStream,
            final ReloadSettings reloadSettings) {
        super(name, path, FileType.YAPION);

        if (create() && inputStream != null) {
            FileUtils.writeToFile(this.file, inputStream);
        }

        if (reloadSettings != null) {
            this.reloadSettings = reloadSettings;
        }

        forceReload();
    }

    @Override
    @SuppressWarnings({"unchecked"})
    protected Map<String, Object> readToMap() throws IOException {
        YAPIONObject yapionObject = YAPIONParser.parse(new FileInputStream(file));
        Object o = YAPIONDeserializer.deserialize(yapionObject);
        if (!(o instanceof Map<?, ?>)) {
            throw new YAPIONException();
        }
        return (Map<String, Object>)o;
    }

    @Override
    protected void write(FileData data) throws IOException {
        YAPIONSerializer.serialize(data.toMap()).toYAPION(new FileOutput(file));
    }

}
