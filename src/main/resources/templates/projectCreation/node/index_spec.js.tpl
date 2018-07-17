import chai from 'chai'
import chaiHttp from 'chai-http'
import app from '../src'

const expect = chai.expect
chai.use(chaiHttp)

describe ('/', () => {
	it ('responds with status 200', (done) => {
		chai.request(app)
			.get('/')
			.end((req, res) => {
				expect(res).to.have.status(200)
				expect(res.text).to.equal('Hello World!')
				done()
			})
	})
})