import socket
import json

class SparkClient:
    def __init__(self, host='localhost', port=8080):
        self.host = host
        self.port = port

    # submit jobs
    def submit_job(self, data, operations):
        payload = {
            "data": data,
            "operations": operations
        }

        try:
            with socket.create_connection((self.host, self.port)) as sock:
                # Send job request
                message = json.dumps(payload) + "\n"
                sock.sendall(message.encode('utf-8'))

                # Receive response
                response = self._recv_all(sock)
                result = json.loads(response)
                return result

        except ConnectionRefusedError:
            print("[Client] Connection failed. Is the Java server running?")
            return None

    # receive input
    def _recv_all(self, sock):
        """
        Receives data until the connection is closed.
        """
        buffer = b""
        while True:
            part = sock.recv(4096)
            if not part:
                break
            buffer += part
        return buffer.decode('utf-8').strip()