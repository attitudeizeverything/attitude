package jcifs.smb;

import com.squareup.okhttp.internal.http.HttpTransport;
import java.io.IOException;

class TransactNamedPipeOutputStream extends SmbFileOutputStream {
    private boolean dcePipe;
    private String path;
    private SmbNamedPipe pipe;
    private byte[] tmp;

    TransactNamedPipeOutputStream(SmbNamedPipe pipe) throws IOException {
        boolean z = true;
        super(pipe, false, (pipe.pipeType & -65281) | 32);
        this.tmp = new byte[1];
        this.pipe = pipe;
        if ((pipe.pipeType & SmbNamedPipe.PIPE_TYPE_DCE_TRANSACT) != SmbNamedPipe.PIPE_TYPE_DCE_TRANSACT) {
            z = false;
        }
        this.dcePipe = z;
        this.path = pipe.unc;
    }

    public void close() throws IOException {
        this.pipe.close();
    }

    public void write(int b) throws IOException {
        this.tmp[0] = (byte) b;
        write(this.tmp, 0, 1);
    }

    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (len < 0) {
            len = 0;
        }
        if ((this.pipe.pipeType & SmbNamedPipe.PIPE_TYPE_CALL) == SmbNamedPipe.PIPE_TYPE_CALL) {
            this.pipe.send(new TransWaitNamedPipe(this.path), new TransWaitNamedPipeResponse());
            this.pipe.send(new TransCallNamedPipe(this.path, b, off, len), new TransCallNamedPipeResponse(this.pipe));
        } else if ((this.pipe.pipeType & SmbNamedPipe.PIPE_TYPE_TRANSACT) == SmbNamedPipe.PIPE_TYPE_TRANSACT) {
            ensureOpen();
            TransTransactNamedPipe req = new TransTransactNamedPipe(this.pipe.fid, b, off, len);
            if (this.dcePipe) {
                req.maxDataCount = HttpTransport.DEFAULT_CHUNK_LENGTH;
            }
            this.pipe.send(req, new TransTransactNamedPipeResponse(this.pipe));
        }
    }
}
