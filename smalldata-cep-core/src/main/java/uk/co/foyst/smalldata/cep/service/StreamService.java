package uk.co.foyst.smalldata.cep.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.foyst.smalldata.cep.Stream;
import uk.co.foyst.smalldata.cep.StreamId;
import uk.co.foyst.smalldata.cep.adapter.CEPAdapter;
import uk.co.foyst.smalldata.cep.dao.StreamView;
import uk.co.foyst.smalldata.cep.dao.StreamViewDao;
import uk.co.foyst.smalldata.cep.dao.StreamViewFactory;
import uk.co.foyst.smalldata.cep.exception.EntityNotFoundException;

import java.util.List;

@Service
public class StreamService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String NOT_EXISTS_MESSAGE = "Stream with Id '%s' does not exist.";
    private static final String NAME_NOT_EXISTS_MESSAGE = "Stream with name '%s' does not exist.";

    private final StreamViewDao streamViewDao;
    private final StreamViewFactory streamViewFactory;
    private final CEPAdapter cepAdapter;

    @Autowired
    public StreamService(final StreamViewDao streamViewDao, final StreamViewFactory streamViewFactory, final CEPAdapter cepAdapter) {

        this.streamViewDao = streamViewDao;
        this.streamViewFactory = streamViewFactory;
        this.cepAdapter = cepAdapter;
    }

    public List<Stream> readAll() throws Exception {

        return streamViewFactory.convertToStream(streamViewDao.findAll());
    }

    public Stream add(final Stream stream) throws Exception {

        log.info("add: stream={}", stream);

        cepAdapter.define(stream);

        final StreamView streamView = streamViewFactory.build(stream);
        streamViewDao.save(streamView);

        return stream;
    }

    public Stream read(final StreamId id) {

        final String streamId = id.toString();
        final StreamView streamView = streamViewDao.findOne(streamId);
        if (streamView == null) {
            throw new EntityNotFoundException(String.format(NOT_EXISTS_MESSAGE, id.toString()));
        }
        return streamViewFactory.convertToStream(streamView);
    }

    public Stream readByName(final String streamName) {

        final StreamView streamView = streamViewDao.findByName(streamName);
        if (streamView == null) {
            throw new EntityNotFoundException(String.format(NOT_EXISTS_MESSAGE, streamName));
        }
        return streamViewFactory.convertToStream(streamView);
    }

    public Stream update(final Stream updatedStream) {

        final StreamView currentStreamView = streamViewDao.findOne(updatedStream.getStreamId().toString());
        if (currentStreamView == null) {
            throw new EntityNotFoundException(String.format(NOT_EXISTS_MESSAGE, updatedStream.getStreamId().toString()));
        }
        cepAdapter.update(updatedStream);

        final StreamView streamView = streamViewFactory.build(updatedStream);
        streamViewDao.save(streamView);

        return updatedStream;
    }

    public void delete(final Stream stream) throws Exception {

        cepAdapter.remove(stream);
        final StreamView streamView = streamViewFactory.build(stream);
        streamViewDao.delete(streamView);
    }
}