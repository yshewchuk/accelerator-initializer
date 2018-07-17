import express from 'express'
const app = express()

app.get('/', (req, res) => {
	res.set('Content-Type', 'text/plain')
  	res.send('Hello World!')
})

app.listen(8080)

export default app