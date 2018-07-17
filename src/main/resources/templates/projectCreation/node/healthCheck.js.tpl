import chai from 'chai'
import chaiAsPromised from 'chai-as-promised'
chai.use(chaiAsPromised)
import { expect } from 'chai'

describe('', () => {
	before(() => {
		browser.get('/')
		browser.wait(() => {
            return $('pre').isPresent().then((b) => {
            	return b
            })
        }, 10000)
	})
    it('should display hello world', () => {
        expect($('pre').getText()).to.eventually.equal('Hello World!')
    })
})